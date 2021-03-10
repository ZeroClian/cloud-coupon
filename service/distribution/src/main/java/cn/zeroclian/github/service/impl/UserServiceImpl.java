package cn.zeroclian.github.service.impl;

import cn.zeroclian.github.constant.Constant;
import cn.zeroclian.github.constant.CouponStatus;
import cn.zeroclian.github.dao.CouponDao;
import cn.zeroclian.github.entity.Coupon;
import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.feign.SettlementClient;
import cn.zeroclian.github.feign.TemplateClient;
import cn.zeroclian.github.service.IRedisService;
import cn.zeroclian.github.service.IUserService;
import cn.zeroclian.github.vo.*;
import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Desciption 用户服务相关的接口实现
 * 所有的操作过程，状态都保存在 Redis 中，并通过 Kafka 把消息传递到 Mysql 中
 * @Author ZeroClian
 * @Date 2021-03-04-15:35
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final CouponDao couponDao;
    /**
     * redis客户端
     */
    private final IRedisService redisService;
    /**
     * 模板微服务客户端
     */
    private final TemplateClient templateClient;
    /**
     * 结算微服务客户端
     */
    private final SettlementClient settlementClient;
    /**
     * Kafka 客户端
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public UserServiceImpl(CouponDao couponDao, IRedisService redisService,
                           TemplateClient templateClient, SettlementClient settlementClient,
                           KafkaTemplate<String, String> kafkaTemplate) {
        this.couponDao = couponDao;
        this.redisService = redisService;
        this.templateClient = templateClient;
        this.settlementClient = settlementClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException {
        List<Coupon> curCached = redisService.getCachedCoupons(userId, status);
        List<Coupon> preTarget;
        if (CollectionUtils.isNotEmpty(curCached)) {
            log.debug("coupon cache is not empty: {}, {}", userId, status);
            preTarget = curCached;
        } else {
            log.debug("coupon cache is empty, get coupon from db: {}, {}", userId, status);
            List<Coupon> dbCoupon = couponDao.findAllByUserIdAndStatus(userId, CouponStatus.of(status));
            //如果数据库中没有记录，直接返回就看可以，Cache 中已经加入了一张无效的优惠券
            if (CollectionUtils.isEmpty(dbCoupon)) {
                log.debug("current user do not have coupon: {}, {}", userId, status);
                return dbCoupon;
            }
            //填充 dbCoupons 的 templateSDK 字段
            Map<Integer, CouponTemplateSDK> id2TemplateSDK =
                    templateClient.findIds2TemplateSDK(
                            dbCoupon.stream()
                                    .map(Coupon::getTemplateId)
                                    .collect(Collectors.toList())
                    ).getData();
            dbCoupon.forEach(dc -> dc.setTemplateSDK(id2TemplateSDK.get(dc.getTemplateId())));
            //数据库中存在记录
            preTarget = dbCoupon;
            //将记录写入 Cache
            redisService.addCouponToCache(userId, preTarget, status);
        }
        //将无效优惠券剔除
        preTarget = preTarget.stream()
                .filter(c -> c.getId() != -1)
                .collect(Collectors.toList());
        //如果当前获取的是可用优惠券，还需要对已过期优惠券的延迟处理
        if (CouponStatus.of(status) == CouponStatus.USABLE){
            CouponClassify classify = CouponClassify.classify(preTarget);
            //如果已过期状态不为空，需要做延迟处理
            if (CollectionUtils.isNotEmpty(classify.getExpired())){
                log.info("add expire coupons to cache from findCouponsByStatus: {}, {}",
                        userId,status);
                redisService.addCouponToCache(
                        userId,classify.getExpired(),CouponStatus.EXPIRED.getCode());
                //发送到 Kafka 中做异步处理
                kafkaTemplate.send(Constant.TOPIC,
                        JSON.toJSONString(new CouponKafkaMessage(
                                CouponStatus.EXPIRED.getCode(),
                                classify.getExpired().stream().map(Coupon::getId).collect(Collectors.toList()))));

            }
            return classify.getUsable();
        }
        return preTarget;
    }

    @Override
    public List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException {

        long curTime = new Date().getTime();
        List<CouponTemplateSDK> templateSDKS = templateClient.findAllUsableTemplate().getData();
        log.debug("find all template(From TemplateClient) count: {}",templateSDKS.size());

        //过滤过期的优惠券模板
        templateSDKS = templateSDKS.stream()
                .filter(t -> t.getRule().getExpiration().getDeadline() > curTime).collect(Collectors.toList());

        log.info("find usable template count: {}",templateSDKS.size());

        //key 是 templateId
        //value 中的 left 是 Template limitation, right 是优惠券模板
        Map<Integer, Pair<Integer,CouponTemplateSDK>> limit2Template = new HashMap<>(templateSDKS.size());

        templateSDKS.forEach(t ->
                limit2Template.put(t.getId(),Pair.of(t.getRule().getLimitation(),t))
        );
        List<CouponTemplateSDK> result = new ArrayList<>(limit2Template.size());
        List<Coupon> userUsableCoupons = findCouponsByStatus(userId,CouponStatus.USABLE.getCode());
        log.debug("current user has usable coupons: {}",userUsableCoupons.size());

        //key 是 templateId
        Map<Integer,List<Coupon>> templateId2Coupons = userUsableCoupons.stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));
        //根据 Template 的 Rule 判断是否可以领取优惠券模板
        limit2Template.forEach((k,v) ->{
            int limitation = v.getLeft();
            CouponTemplateSDK templateSDK = v.getRight();
            if (templateId2Coupons.containsKey(k) && templateId2Coupons.get(k).size() >= limitation){
                return;
            }
            result.add(templateSDK);
        });
        return result;
    }

    /**
     * 用户领取优惠券
     * 1. 从 TemplateClient 拿到对应的优惠券
     * 2. 根据 limitation 判断用户是否可以领取
     * 3. save to db
     * 4. 填充 CouponTemplateSDK
     * 5. save to cache
     * @param request {@link AcquireTemplateRequest}
     * @return
     * @throws CouponException
     */
    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException {
        Map<Integer,CouponTemplateSDK> id2Template = templateClient.findIds2TemplateSDK(
                Collections.singletonList(request.getTemplateSDK().getId())
        ).getData();

        //优惠券模板是需要存在的
        if (id2Template.size() <= 0){
            log.error("can not acquire template from templateClient：{}",request.getTemplateSDK().getId());
            throw new CouponException("can not acquire template from templateClient");
        }

        //用户是否可以领取这张优惠券
        List<Coupon> userUsableCoupons = findCouponsByStatus(request.getUserId(),CouponStatus.USABLE.getCode());
        Map<Integer,List<Coupon>> templateId2Coupons = userUsableCoupons.stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));
        if (templateId2Coupons.containsKey(request.getTemplateSDK().getId())
        && templateId2Coupons.get(request.getTemplateSDK().getId()).size() >=
                request.getTemplateSDK().getRule().getLimitation()){
            log.error("exceed template assign limitation: {}",request.getTemplateSDK().getId());
            throw new CouponException("exceed template assign limitation");
        }

        //尝试去获取优惠券码
        String couponCode = redisService.tryToAcquireCouponCodeFromCache(request.getTemplateSDK().getId());
        if (StringUtils.isEmpty(couponCode)){
            log.error("can not acquire coupon code: {}",request.getTemplateSDK().getId());
            throw new CouponException("can not acquire coupon code");
        }
        Coupon newCoupon = new Coupon(
                request.getTemplateSDK().getId(),request.getUserId(),couponCode,CouponStatus.USABLE
        );
        newCoupon = couponDao.save(newCoupon);

        //填充 Coupon 对象的 CouponTemplateSDK，一定要在放入缓存之前去填充
        newCoupon.setTemplateSDK(request.getTemplateSDK());

        //放入缓存中
        redisService.addCouponToCache(
                request.getUserId(),
                Collections.singletonList(newCoupon),
                CouponStatus.USABLE.getCode()
        );
        return newCoupon;
    }

    @Override
    public SettlementInfo settlement(SettlementInfo info) throws CouponException {
        return null;
    }
}

package cn.zeroclian.github.service.impl;

import cn.zeroclian.github.constant.Constant;
import cn.zeroclian.github.constant.CouponStatus;
import cn.zeroclian.github.entity.Coupon;
import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.service.IRedisService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Desciption Redis 相关的操作服务接口实现
 * @Author ZeroClian
 * @Date 2021-03-03-16:31
 */
@Slf4j
@Service
public class RedisServiceImpl implements IRedisService {

    /**
     * Redis 客户端
     */
    private final StringRedisTemplate redisTemplate;

    public RedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据用户id和状态找到缓存的优惠券列表数据
     *
     * @param userId 用户id
     * @param status 优惠券状态
     * @return
     */
    @Override
    public List<Coupon> getCachedCoupons(Long userId, Integer status) {
        log.info("Get Coupons From Cache: {},{}", userId, status);
        String redisKey = status2RedisKey(status, userId);
        List<String> couponStrs = redisTemplate.opsForHash().values(redisKey)
                .stream()
                .map(o -> Objects.toString(o, null))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(couponStrs)) {
            saveEmptyCouponListToCache(userId, Collections.singletonList(status));
            return Collections.emptyList();
        }
        return couponStrs.stream()
                .map(cs -> JSON.parseObject(cs, Coupon.class))
                .collect(Collectors.toList());
    }

    /**
     * 保存空的优惠券列表到缓存中
     * 目的：避免缓存穿透
     *
     * @param userId 用户id
     * @param status 优惠券状态列表
     */
    @Override
    @SuppressWarnings("all")
    public void saveEmptyCouponListToCache(Long userId, List<Integer> status) {
        log.info("Save Empty List To Cache For User: {}, Status:{} ",
                userId, JSON.toJSONString(status));
        //key是coupon_id,value是序列化的Coupon
        Map<String, String> invaildCouponMap = new HashMap<>();
        invaildCouponMap.put("-1", JSON.toJSONString(Coupon.invalidCoupon()));

        //使用SessionCallback把数据命令放入到Redis的pipeline
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                status.forEach(s -> {
                    String redisKey = status2RedisKey(s, userId);
                    operations.opsForHash().putAll(redisKey, invaildCouponMap);
                });
                return null;
            }
        };
        log.info("Pipeline Exe Result: {}",
                JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
    }

    /**
     * 尝试从 Cache 中获取一个优惠券码
     *
     * @param templateId 优惠券模板id
     * @return
     */
    @Override
    public String tryToAcquireCouponCodeFromCache(Integer templateId) {

        String redisKey = String.format("%s%s",
                Constant.RedisPrefix.COUPON_TEMPLATE, templateId.toString());
        //优惠券没有顺序，那边Pop没有关系
        String couponCode = redisTemplate.opsForList().leftPop(redisKey);

        log.info("Acquire Coupon Code: {}, {}, {}", templateId, redisKey, couponCode);
        return couponCode;
    }

    /**
     * 将用户的优惠券保存到 Cache 中
     *
     * @param userId  用户id
     * @param coupons 优惠券
     * @param status  优惠券状态
     * @return
     * @throws CouponException
     */
    @Override
    public Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException {
        log.info("Add Coupon To Cache: {}, {}, {}",
                userId, JSON.toJSONString(coupons), status);
        Integer result = -1;
        CouponStatus couponStatus = CouponStatus.of(status);
        switch (couponStatus) {
            case USABLE:
                result = addCouponToCacheForUsable(userId, coupons);
                break;
            case USED:
                result = addCouponToCacheForUsed(userId, coupons);
                break;
            case EXPIRED:
                result = addCouponToCacheForExpired(userId, coupons);
                break;
        }
        return null;
    }

    /**
     * 新增加到优惠券到 Cache 中
     *
     * @param userId
     * @param coupons
     * @return
     */
    @SuppressWarnings("all")
    private Integer addCouponToCacheForUsable(Long userId, List<Coupon> coupons) {
        //status 是 USABLE ，代表是新增加的优惠券
        //只会影响一个 Cache：USER_COUPON_USABLE
        log.debug("Add Coupon To Cache For Usable.");

        Map<String, String> needCacheObject = new HashMap<>();
        coupons.forEach(coupon -> {
            needCacheObject.put(coupon.getId().toString(), JSON.toJSONString(coupon));
        });
        String redisKey = status2RedisKey(CouponStatus.USABLE.getCode(), userId);
        redisTemplate.opsForHash().putAll(redisKey, needCacheObject);

        log.info("Add {} Coupon To Cache: {}, {}",
                needCacheObject.size(), userId, redisKey);

        redisTemplate.expire(
                redisKey,
                getRandomExpirationTime(1, 2),
                TimeUnit.SECONDS);

        return needCacheObject.size();
    }

    /**
     * 将已使用的优惠券加入到 Cache 中
     *
     * @param userId
     * @param coupons
     * @return
     * @throws CouponException
     */
    @SuppressWarnings("all")
    private Integer addCouponToCacheForUsed(Long userId, List<Coupon> coupons)
            throws CouponException {
        //如果 status 是 USED ，代表用户操作是使用当前的优惠券
        //将影响 USABLE、USED 两个 Cache
        log.debug("Add Coupon To Cache For Used.");

        Map<String, String> needCacheForUsed = new HashMap<>(coupons.size());
        String redisKeyForUsable = status2RedisKey(
                CouponStatus.USABLE.getCode(), userId
        );
        String redisKeyForUsed = status2RedisKey(
                CouponStatus.USED.getCode(), userId
        );
        //获取当前用户可用的优惠券
        List<Coupon> curUsableCoupon = getCachedCoupons(
                userId, CouponStatus.USABLE.getCode()
        );
        //判断当前可用的有优惠券个数一定是大于1
        assert curUsableCoupon.size() > coupons.size();
        coupons.forEach(coupon -> {
            needCacheForUsed.put(coupon.getId().toString(), JSON.toJSONString(coupon));
        });
        //校验当前的优惠券参数是否与 Cache 中的匹配
        List<Integer> curUsableIds = curUsableCoupon.stream()
                .map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());
        if (!CollectionUtils.isSubCollection(paramIds, curUsableIds)) {
            log.error("CurCoupons Is Not Equal Cache: {}, {}, {}",
                    userId, JSON.toJSONString(curUsableIds), JSON.toJSONString(paramIds));
            throw new CouponException("CurCoupons Is Not Equal Cache!");
        }
        List<String> needCleanKey = paramIds.stream()
                .map(i -> i.toString()).collect(Collectors.toList());
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //1.已使用的优惠券 Cache 缓存添加
                operations.opsForHash().putAll(redisKeyForUsed, needCacheForUsed);
                //2.可用的优惠券 Cache 需要清理
                operations.opsForHash().delete(redisKeyForUsable, needCleanKey.toArray());
                //3.重置过期时间
                operations.expire(redisKeyForUsable, getRandomExpirationTime(1, 2), TimeUnit.SECONDS);
                operations.expire(redisKeyForUsed, getRandomExpirationTime(1, 2), TimeUnit.SECONDS);
                return null;
            }
        };
        log.info("Pipeline Exe Result: {}",
                JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
        return coupons.size();
    }

    /**
     * 将过期优惠券加入到 Cache 中
     *
     * @param userId
     * @param coupons
     * @return
     * @throws CouponException
     */
    @SuppressWarnings("all")
    private Integer addCouponToCacheForExpired(Long userId, List<Coupon> coupons) throws CouponException {
        //status 是 EXPIRED，代表是已有的优惠券过期了
        //影响了 USABLE、EXPIRED
        log.debug("Add Coupon To Cache For Expired.");

        //最终需要保存的 Cache
        Map<String, String> needCacheForExpired = new HashMap<>(coupons.size());
        String redisKeyForUsable = status2RedisKey(
                CouponStatus.USABLE.getCode(), userId
        );
        String redisKeyForExpired = status2RedisKey(
                CouponStatus.EXPIRED.getCode(), userId
        );
        List<Coupon> curUsableCoupons = getCachedCoupons(
                userId, CouponStatus.USABLE.getCode()
        );
        //当前可用的优惠券个数一定是大于1的
        assert curUsableCoupons.size() > coupons.size();

        coupons.forEach(c -> {
            needCacheForExpired.put(c.getId().toString(), JSON.toJSONString(c));
        });
        //校验当前的优惠券参数是否与 Cache 中的匹配
        List<Integer> curUsableIds = curUsableCoupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());
        if (!CollectionUtils.isSubCollection(paramIds, curUsableIds)) {
            log.error("CurCoupons Is Not Equal Cache: {}, {}, {}",
                    userId, JSON.toJSONString(curUsableIds), JSON.toJSONString(paramIds));
            throw new CouponException("CurCoupons Is Not Equal Cache!");
        }
        List<String> needCleanKey = paramIds.stream()
                .map(i -> i.toString()).collect(Collectors.toList());
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //1.已过期的优惠券 Cache 缓存
                operations.opsForHash().putAll(redisKeyForExpired, needCacheForExpired);
                //2.可用的优惠券 Cache 需要清理
                operations.opsForHash().delete(redisKeyForUsable, needCleanKey.toArray());
                //3.重置过期时间
                operations.expire(redisKeyForUsable, getRandomExpirationTime(1, 2), TimeUnit.SECONDS);
                operations.expire(redisKeyForExpired, getRandomExpirationTime(1, 2), TimeUnit.SECONDS);
                return null;
            }
        };
        log.info("Pipeline Exe Result: {}",
                JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
        return coupons.size();
    }

    /**
     * 根据 status 获取到对应的 Redis Key
     */
    private String status2RedisKey(Integer status, Long userId) {
        String redisKey = null;
        CouponStatus couponStatus = CouponStatus.of(status);
        switch (couponStatus) {
            case USABLE:
                redisKey = String.format("%s%s",
                        Constant.RedisPrefix.USER_COUPON_USABLE, userId);
                break;
            case USED:
                redisKey = String.format("%s%s",
                        Constant.RedisPrefix.USER_COUPON_USED, userId);
                break;
            case EXPIRED:
                redisKey = String.format("%s%s",
                        Constant.RedisPrefix.USER_COUPON_EXPIRED, userId);
                break;
        }
        return redisKey;
    }

    /**
     * 获取一个随机的过期时间
     * 缓存雪崩： key 在同一时间失效
     *
     * @param min 最小的小时数
     * @param max 最大的小时数
     * @return 返回[min，max]之间的随机秒数
     */
    private Long getRandomExpirationTime(Integer min, Integer max) {
        return RandomUtils.nextLong(min * 60 * 60, max * 60 * 60);
    }
}

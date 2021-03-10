package cn.zeroclian.github.service.impl;

import cn.zeroclian.github.constant.Constant;
import cn.zeroclian.github.constant.CouponStatus;
import cn.zeroclian.github.dao.CouponDao;
import cn.zeroclian.github.entity.Coupon;
import cn.zeroclian.github.service.IKafkaService;
import cn.zeroclian.github.vo.CouponKafkaMessage;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Desciption  Kafka 相关的服务接口实现
 * 核心思想：是将 Cache 中的 Coupon 的状态变化同步到 DB 中
 * @Author ZeroClian
 * @Date 2021-03-03-22:01
 */
@Slf4j
@Component
public class KafkaServiceImpl implements IKafkaService {

    private final CouponDao couponDao;

    public KafkaServiceImpl(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    /**
     * 消费优惠券 Kafka 消息
     * @param record {@link ConsumerRecord}
     */
    @Override
    @KafkaListener(topics = {Constant.TOPIC},groupId = "zeroClian_coupon_1")
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()){
            Object message = kafkaMessage.get();
            CouponKafkaMessage couponInfo = JSON.parseObject(
                    message.toString(),CouponKafkaMessage.class);
            log.info("Receive CouponKafkaMessage: {}",message.toString());
            CouponStatus status = CouponStatus.of(couponInfo.getStatus());
            switch (status){
                case USABLE:
                    break;
                case USED:
                    processUsedCoupon(couponInfo,status);
                    break;
                case EXPIRED:
                    processExpireCoupon(couponInfo,status);
                    break;
            }
        }
    }

    /**
     *处理已使用的用户优惠券
     */
    private void processUsedCoupon(CouponKafkaMessage kafkaMessage,
                                   CouponStatus status){
        // TODO 给用户发送短信
        processCouponByStatus(kafkaMessage,status);
    }
    /**
     *处理过期的用户优惠券
     */
    private void processExpireCoupon(CouponKafkaMessage kafkaMessage,
                                   CouponStatus status){
        // TODO 给用户发送推送
        processCouponByStatus(kafkaMessage,status);
    }

    /**
     * 根据状态处理优惠券信息
     */
    private void processCouponByStatus(CouponKafkaMessage kafkaMessage,
                                       CouponStatus status){
        List<Coupon> coupons = couponDao.findAllById(kafkaMessage.getIds());
        if (CollectionUtils.isEmpty(coupons) || coupons.size() != kafkaMessage.getIds().size()){
            log.error("Can Not Find Right Coupon Info: {}",
                    JSON.toJSONString(kafkaMessage));
            // TODO 发送邮件
            return;
        }
        coupons.forEach(coupon -> coupon.setStatus(status));
        log.info("CouponKafkaMessage Op Coupon Count: {}", couponDao.saveAll(coupons).size());
    }
}

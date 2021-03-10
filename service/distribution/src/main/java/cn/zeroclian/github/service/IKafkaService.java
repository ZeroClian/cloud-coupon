package cn.zeroclian.github.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @Desciption Kafka 相关的服务接口定义
 * @Author ZeroClian
 * @Date 2021-03-03-15:32
 */
public interface IKafkaService {
    /**
     * 消费优惠券 Kafka 消息
     *
     * @param record {@link ConsumerRecord}
     */
    void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record);
}

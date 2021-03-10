package cn.zeroclian.github.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Desciption 优惠券 Kafka 消息对象定义
 * @Author ZeroClian
 * @Date 2021-03-03-22:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponKafkaMessage {

    /**
     * 优惠券状态
     */
    private Integer status;
    /**
     * Coupon 主键
     */
    private List<Integer> ids;
}

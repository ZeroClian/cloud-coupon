package cn.zeroclian.github.constant;

/**
 * @Desciption  常用常量定义
 * @Author ZeroClian
 * @Date 2021-02-26-15:02
 */
public class Constant {

    /** Kafka 消息的 Topic */
    public static final String TOPIC = "user_coupon_op";

    /**
     * <h2>Redis Key 前缀定义</h2>
     * */
    public static class RedisPrefix {

        /** 优惠券码 key 前缀 */
        public static final String COUPON_TEMPLATE =
                "coupon_template_code_";

        /** 用户当前所有可用的优惠券 key 前缀 */
        public static final String USER_COUPON_USABLE =
                "user_coupon_usable_";

        /** 用户当前所有已使用的优惠券 key 前缀 */
        public static final String USER_COUPON_USED =
                "user_coupon_used_";

        /** 用户当前所有已过期的优惠券 key 前缀 */
        public static final String USER_COUPON_EXPIRED =
                "user_coupon_expired_";
    }
}


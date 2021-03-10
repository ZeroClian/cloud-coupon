package cn.zeroclian.github.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Desciption  用户优惠券的状态
 * @Author ZeroClian
 * @Date 2021-03-01-18:34
 */
@Getter
@AllArgsConstructor
public enum CouponStatus {

    USABLE("",1),
    USED("",2),
    EXPIRED("",3);

    /** 优惠券状态描述信息 */
    private String description;
    /** 优惠券状态码*/
    private Integer code;

    /**
     * 根据 code获取到 CouponStatus
     * @param code
     * @return
     */
    public static CouponStatus of(Integer code){
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(code+"not exists"));
    }
}

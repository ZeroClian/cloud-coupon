package cn.zeroclian.github.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Desciption  优惠券分类
 * @Author ZeroClian
 * @Date 2021-02-26-14:59
 */
@Getter
@AllArgsConstructor
public enum CouponCategory {

    MANJIAN("满减券", "001"),
    ZHEKOU("折扣券", "002"),
    LIJIAN("立减券", "003");

    /** 优惠券描述(分类) */
    private String description;

    /** 优惠券分类编码 */
    private String code;

    /**
     * 根据code返回对应的枚举
     * @param code
     * @return
     */
    public static CouponCategory of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}


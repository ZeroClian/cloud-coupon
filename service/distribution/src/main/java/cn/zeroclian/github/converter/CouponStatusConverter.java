package cn.zeroclian.github.converter;

import cn.zeroclian.github.constant.CouponStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Desciption  优惠券状态枚举属性转换器
 * @Author ZeroClian
 * @Date 2021-03-01-22:03
 */
@Converter
public class CouponStatusConverter implements AttributeConverter<CouponStatus,Integer> {
    @Override
    public Integer convertToDatabaseColumn(CouponStatus couponStatus) {
        return couponStatus.getCode();
    }

    @Override
    public CouponStatus convertToEntityAttribute(Integer integer) {
        return CouponStatus.of(integer);
    }
}

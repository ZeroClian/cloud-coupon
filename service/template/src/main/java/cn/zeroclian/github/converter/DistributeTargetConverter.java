package cn.zeroclian.github.converter;

import cn.zeroclian.github.constant.DistributeTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Desciption  分发目标枚举属性转换器
 * @Author ZeroClian
 * @Date 2021-02-26-15:23
 */
@Converter
public class DistributeTargetConverter
        implements AttributeConverter<DistributeTarget, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DistributeTarget distributeTarget) {
        return distributeTarget.getCode();
    }

    @Override
    public DistributeTarget convertToEntityAttribute(Integer code) {
        return DistributeTarget.of(code);
    }
}

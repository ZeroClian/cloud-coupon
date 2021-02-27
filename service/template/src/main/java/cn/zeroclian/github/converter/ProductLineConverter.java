package cn.zeroclian.github.converter;

import cn.zeroclian.github.constant.ProductLine;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Desciption  产品线枚举属性转换器
 * @Author ZeroClian
 * @Date 2021-02-26-15:24
 */
@Converter
public class ProductLineConverter
        implements AttributeConverter<ProductLine, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProductLine productLine) {
        return productLine.getCode();
    }

    @Override
    public ProductLine convertToEntityAttribute(Integer code) {
        return ProductLine.of(code);
    }
}

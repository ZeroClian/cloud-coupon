package cn.zeroclian.github.converter;

import cn.zeroclian.github.vo.TemplateRule;
import com.alibaba.fastjson.JSON;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Desciption  优惠券规则属性转换器
 * @Author ZeroClian
 * @Date 2021-02-26-15:25
 */
@Converter
public class RuleConverter
        implements AttributeConverter<TemplateRule, String> {

    @Override
    public String convertToDatabaseColumn(TemplateRule rule) {
        return JSON.toJSONString(rule);
    }

    @Override
    public TemplateRule convertToEntityAttribute(String rule) {
        return JSON.parseObject(rule, TemplateRule.class);
    }
}


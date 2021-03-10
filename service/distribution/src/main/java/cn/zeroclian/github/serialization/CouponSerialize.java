package cn.zeroclian.github.serialization;

import cn.zeroclian.github.entity.Coupon;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @Desciption  优惠券实体类自定义序列化器
 * @Author ZeroClian
 * @Date 2021-03-01-22:07
 */
public class CouponSerialize extends JsonSerializer<Coupon> {
    @Override
    public void serialize(Coupon coupon, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        //开始序列化
        gen.writeStartObject();
        gen.writeStringField("id",coupon.getId().toString());
        gen.writeStringField("templateId",coupon.getTemplateId().toString());
        gen.writeStringField("userId",coupon.getUserId().toString());
        gen.writeStringField("couponCode",coupon.getCouponCode());
        gen.writeStringField("assignTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(coupon.getAssignTime()));
        gen.writeStringField("name",coupon.getTemplateSDK().getName());
        gen.writeStringField("logo",coupon.getTemplateSDK().getLogo());
        gen.writeStringField("desc",coupon.getTemplateSDK().getDesc());
        gen.writeStringField("expiration", JSON.toJSONString(coupon.getTemplateSDK().getRule().getExpiration()));
        gen.writeStringField("discount",JSON.toJSONString(coupon.getTemplateSDK().getRule().getDiscount()));
        gen.writeStringField("usage",JSON.toJSONString(coupon.getTemplateSDK().getRule().getUsage()));
        //结束序列化
        gen.writeEndObject();
    }
}

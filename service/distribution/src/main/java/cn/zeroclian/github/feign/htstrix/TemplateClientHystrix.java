package cn.zeroclian.github.feign.htstrix;

import cn.zeroclian.github.feign.TemplateClient;
import cn.zeroclian.github.vo.CommonResponse;
import cn.zeroclian.github.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Desciption 优惠券模板 Feign 的熔断降级策略
 * @Author ZeroClian
 * @Date 2021-03-04-15:02
 */
@Slf4j
@Component
public class TemplateClientHystrix implements TemplateClient {
    @Override
    public CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate() {
        log.error("[eureka-clien-coupon-template] findAllUsableTemplate request error!");
        return new CommonResponse<>(-1,
                "[eureka-clien-coupon-template] request error!",
                Collections.emptyList());
    }

    @Override
    public CommonResponse<Map<Integer, CouponTemplateSDK>> findIds2TemplateSDK(Collection<Integer> ids) {
        log.error("[eureka-clien-coupon-template] findIds2TemplateSDK request error!");
        return new CommonResponse<>(-1,
                "[eureka-clien-coupon-template] request error!",
                new HashMap<>());
    }
}

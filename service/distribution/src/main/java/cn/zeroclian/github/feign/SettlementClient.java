package cn.zeroclian.github.feign;

import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.feign.htstrix.SettlementClientHystrix;
import cn.zeroclian.github.vo.CommonResponse;
import cn.zeroclian.github.vo.SettlementInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Desciption 优惠券结算微服务 Feign 接口定义
 * @Author ZeroClian
 * @Date 2021-03-04-14:54
 */
@FeignClient(value = "eureka-client-coupon-settlement",
        fallback = SettlementClientHystrix.class)
public interface SettlementClient {

    /**
     * 优惠券规则计算
     */
    @RequestMapping(value = "/coupon-settlement/settlement/compute", method = RequestMethod.POST)
    CommonResponse<SettlementInfo> computeRule(
            @RequestBody SettlementInfo settlement) throws CouponException;
}

package cn.zeroclian.github.feign.htstrix;

import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.feign.SettlementClient;
import cn.zeroclian.github.vo.CommonResponse;
import cn.zeroclian.github.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Desciption  结算微服务熔断策略实现
 * @Author ZeroClian
 * @Date 2021-03-04-15:09
 */
@Slf4j
@Component
public class SettlementClientHystrix implements SettlementClient {
    @Override
    public CommonResponse<SettlementInfo> computeRule(SettlementInfo settlement) throws CouponException {
        log.error("[eureka-clien-coupon-settlement] computeRule request error!");
        settlement.setEmploy(false);
        settlement.setCost(-1.0);
        return new CommonResponse<>(
                -1,
                "[eureka-clien-coupon-settlement] request error!",
                settlement
        );
    }
}

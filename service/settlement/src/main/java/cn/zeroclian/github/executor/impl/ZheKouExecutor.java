package cn.zeroclian.github.executor.impl;

import cn.zeroclian.github.constant.RuleFlag;
import cn.zeroclian.github.executor.AbstractExecutor;
import cn.zeroclian.github.executor.RuleExecutor;
import cn.zeroclian.github.vo.CouponTemplateSDK;
import cn.zeroclian.github.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Desciption 折扣优惠券结算规则执行器
 * @Author ZeroClian
 * @Date 2021-03-10-15:13
 */
@Slf4j
@Component
public class ZheKouExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.ZHEKOU;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {
        double goodsSum = retain2Decimals(goodsCostSum(settlement.getGoodsInfos()));
        SettlementInfo probabiltity = processGoodsTypeNotSatisfy(settlement, goodsSum);
        if (null != probabiltity) {
            log.info("ZheKou Template is not Match to GoodsType!");
            return probabiltity;
        }

        //折扣优惠券可以直接使用，没有门槛
        CouponTemplateSDK templateSDK = settlement.getCouponAndTemplateInfos().get(0).getTemplate();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        //计算使用优惠券之后的价格
        settlement.setCost(
                retain2Decimals((goodsSum * (quota * 1.0 / 100))) > minCost() ?
                        retain2Decimals(goodsSum * (quota * 1.0 / 100)) : minCost()
        );
        log.info("use ZheKou coupon make goods cost from {} to {}",
                goodsSum,settlement.getCost());
        return settlement;
    }
}

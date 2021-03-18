package cn.zeroclian.github.executor.impl;

import cn.zeroclian.github.constant.RuleFlag;
import cn.zeroclian.github.executor.AbstractExecutor;
import cn.zeroclian.github.executor.RuleExecutor;
import cn.zeroclian.github.vo.CouponTemplateSDK;
import cn.zeroclian.github.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Desciption 立减优惠券结算规则执行器
 * @Author ZeroClian
 * @Date 2021-03-10-15:25
 */
@Slf4j
@Component
public class LiJianExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.LIJIAN;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {
        double goodsSum = retain2Decimals(goodsCostSum(settlement.getGoodsInfos()));
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlement, goodsSum);
        if (null != probability) {
            log.info("LiJian Template is not match to GoodsType！");
            return probability;
        }

        //立减优惠券直接使用，没有门槛
        CouponTemplateSDK templateSDK = settlement.getCouponAndTemplateInfos().get(0).getTemplate();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        //计算使用优惠券后的价格
        settlement.setCost(
                retain2Decimals(goodsSum - quota) > minCost() ?
                        retain2Decimals(goodsSum - quota) : minCost()
        );
        log.info("user LiJian coupon make goods cost from {} to {}",
                goodsSum,settlement.getCost());
        return settlement;
    }
}

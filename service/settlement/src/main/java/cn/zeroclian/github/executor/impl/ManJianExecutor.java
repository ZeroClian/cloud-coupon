package cn.zeroclian.github.executor.impl;

import cn.zeroclian.github.constant.RuleFlag;
import cn.zeroclian.github.executor.AbstractExecutor;
import cn.zeroclian.github.executor.RuleExecutor;
import cn.zeroclian.github.vo.CouponTemplateSDK;
import cn.zeroclian.github.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @Desciption 满减优惠券结算规则执行器
 * @Author ZeroClian
 * @Date 2021-03-10-14:56
 */
@Component
@Slf4j
public class ManJianExecutor extends AbstractExecutor implements RuleExecutor {

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {

        double goodsSum = retain2Decimals(goodsCostSum(settlement.getGoodsInfos()));
        SettlementInfo probability = processGoodsTypeNotSatisfy(
                settlement, goodsSum
        );
        if (null != probability) {
            log.info("ManJian Template is not match to GoodsType!");
            return probability;
        }
        //判断满减是否符合折扣标准
        CouponTemplateSDK templateSDK = settlement.getCouponAndTemplateInfos().get(0).getTemplate();
        double base = (double) templateSDK.getRule().getDiscount().getBase();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        //如果不符合标准，则直接返回商品总价
        if (goodsSum < base) {
            log.info("current goods sum < ManJian Coupon Base!");
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfos(Collections.emptyList());
            return settlement;
        }
        //计算使用优惠券之后的价格 - 结算
        settlement.setCost(retain2Decimals(
                (goodsSum - quota) > minCost() ? (goodsSum - quota) : minCost()
        ));
        log.info("Use ManJian Coupon Goods Cost From: {} To {}", goodsSum, settlement.getCost());

        return settlement;
    }
}

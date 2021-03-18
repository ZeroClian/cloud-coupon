package cn.zeroclian.github.executor.impl;

import cn.zeroclian.github.constant.CouponCategory;
import cn.zeroclian.github.constant.RuleFlag;
import cn.zeroclian.github.executor.AbstractExecutor;
import cn.zeroclian.github.executor.RuleExecutor;
import cn.zeroclian.github.vo.GoodsInfo;
import cn.zeroclian.github.vo.SettlementInfo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.language.bm.RuleType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Desciption 满减+折扣优惠券结算规则执行器
 * @Author ZeroClian
 * @Date 2021-03-13-13:55
 */
@Slf4j
@Component
public class ManJianZheKouExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN_ZHEKOU;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {
        double goodsSum = retain2Decimals(goodsCostSum(settlement.getGoodsInfos()));
        //商品类型校验
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlement, goodsSum);
        if (null != probability) {
            log.info("ManJian and ZheKou template is not match to goodsType!");
            return probability;
        }
        SettlementInfo.CouponAndTemplateInfo manJian = null;
        SettlementInfo.CouponAndTemplateInfo zheKou = null;

        for (SettlementInfo.CouponAndTemplateInfo ct : settlement.getCouponAndTemplateInfos()) {
            if (CouponCategory.of(ct.getTemplate().getCategory()) == CouponCategory.MANJIAN) {
                manJian = ct;
            } else {
                zheKou = ct;
            }
        }
        assert null != manJian;
        assert null != zheKou;

        //当前的优惠券和满减券不能一起使用，清空优惠券，返回商品原价
        if (!isTemplateCanShared(manJian,zheKou)){
            log.info("Current ManJian And ZheKou Can Not Shared!");
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfos(Collections.emptyList());
            return settlement;
        }
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = new ArrayList<>();
        double manJianBase = (double) manJian.getTemplate().getRule().getDiscount().getBase();
        double manJianQuota = (double) manJian.getTemplate().getRule().getDiscount().getQuota();
        //最终价格
        double targetSum = goodsSum;
        //先计算满减
        if (targetSum >= manJianBase){
            targetSum -= manJianQuota;
            ctInfos.add(manJian);
        }
        //再计算折扣
        double zheKouQuota = (double) zheKou.getTemplate().getRule().getDiscount().getQuota();
        targetSum *= zheKouQuota*1.0/100;
        ctInfos.add(zheKou);
        settlement.setCouponAndTemplateInfos(ctInfos);
        settlement.setCost(retain2Decimals(targetSum>minCost() ? targetSum:minCost()));
        log.info("Use ManJian And ZheKou Coupon Make Goods Cost From {} to {}",
                goodsSum,settlement.getCost());
        return settlement;
    }

    @Override
    @SuppressWarnings("all")
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlement) {
        log.debug("check ManJian and ZheKou is match or not！");
        List<Integer> goodsType = settlement.getGoodsInfos().stream()
                .map(GoodsInfo::getType).collect(Collectors.toList());
        List<Integer> templateGoodsType = new ArrayList<>();
        settlement.getCouponAndTemplateInfos().forEach(ct -> {
            templateGoodsType.addAll(JSON.parseObject(
                    ct.getTemplate().getRule().getUsage().getGoodsType(),
                    List.class));
        });

        //如果想要使用多累优惠券，则必须要所有商品信息都包含在内，即差集为空
        return CollectionUtils.isEmpty(CollectionUtils.subtract(
                goodsType, templateGoodsType
        ));
    }

    /**
     * 当前的两张优惠券是否可以共用
     * @param manJian
     * @param zheKou
     * @return
     */
    @SuppressWarnings("all")
    private boolean isTemplateCanShared(SettlementInfo.CouponAndTemplateInfo manJian,
                                        SettlementInfo.CouponAndTemplateInfo zheKou){
        String manJianKey = manJian.getTemplate().getKey()
                + String.format("%04d",manJian.getTemplate().getId());
        String zheKouKey = zheKou.getTemplate().getKey()
                + String.format("%04d",zheKou.getTemplate().getId());

        List<String> allSharedKeysForManJian = new ArrayList<>();
        allSharedKeysForManJian.add(manJianKey);
        allSharedKeysForManJian.addAll(JSON.parseObject(
                manJian.getTemplate().getRule().getWeight(),
                List.class
        ));

        List<String> allSharedKeysForZheKou = new ArrayList<>();
        allSharedKeysForZheKou.add(zheKouKey);
        allSharedKeysForZheKou.addAll(JSON.parseObject(
                zheKou.getTemplate().getRule().getWeight(),
                List.class
        ));

        return CollectionUtils.isSubCollection(
                Arrays.asList(manJianKey,zheKouKey), allSharedKeysForManJian)
                || CollectionUtils.isSubCollection(
                        Arrays.asList(manJianKey,zheKouKey),allSharedKeysForZheKou);
    }
}

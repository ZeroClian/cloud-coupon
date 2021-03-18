package cn.zeroclian.github.executor;

import cn.zeroclian.github.constant.RuleFlag;
import cn.zeroclian.github.vo.SettlementInfo;

/**
 * @Desciption  优惠券模板规则处理器规则定义
 * @Author ZeroClian
 * @Date 2021-03-10-14:18
 */
public interface RuleExecutor {

    /**
     * 规则类型标记
     * @return {@link RuleFlag}
     */
    RuleFlag ruleConfig();

    /**
     * 优惠券规则的计算
     * @param settlement {@link SettlementInfo} 包含了选择的优惠券
     * @return {@link SettlementInfo} 修正过的计算信息
     */
    SettlementInfo computeRule(SettlementInfo settlement);
}

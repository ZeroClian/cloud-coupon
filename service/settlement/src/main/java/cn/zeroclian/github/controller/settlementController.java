package cn.zeroclian.github.controller;

import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.executor.ExecuteManager;
import cn.zeroclian.github.vo.SettlementInfo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Desciption  结算服务 Controller
 * @Author ZeroClian
 * @Date 2021-03-15-21:53
 */
@Slf4j
@RestController
public class settlementController {
    //结算规则执行管理器
    private final ExecuteManager executeManager;

    @Autowired
    public settlementController(ExecuteManager executeManager) {
        this.executeManager = executeManager;
    }

    /**
     * 优惠券结算
     * 127.0.0.1:7003/coupon-settlement/settlement/compute
     * 127.0.0.1:9000/zeroclian/coupon-settlement/settlement/compute
     */
    @PostMapping("/settlement/compute")
    public SettlementInfo computeRule(@RequestBody SettlementInfo settlement) throws CouponException{
        log.info("settlement: {}", JSON.toJSONString(settlement));
        return executeManager.computeRule(settlement);
    }
}

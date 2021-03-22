package cn.zeroclian.github.controller;

import cn.zeroclian.github.entity.Coupon;
import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.service.IUserService;
import cn.zeroclian.github.vo.AcquireTemplateRequest;
import cn.zeroclian.github.vo.CouponTemplateSDK;
import cn.zeroclian.github.vo.SettlementInfo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Desciption 用户服务的 Controller
 * @Author ZeroClian
 * @Date 2021-03-22-9:20
 */
@Slf4j
@RestController
public class UserServiceController {
    private final IUserService userService;

    @Autowired
    public UserServiceController(IUserService userService) {
        this.userService = userService;
    }
    /**
     * 根据用户id和状态查询优惠券记录
     * 127.0.0.1:7002/coupon-distribution/coupons
     * 127.0.0.1:9000/zeroclian/coupon-distribution/coupons
     */
    @GetMapping("/coupons")
    public List<Coupon> findCouponsByStatus(@RequestParam("userId") Long userId,
                                            @RequestParam("status") Integer status) throws CouponException {
        log.info("Find Coupons By Status:{}, {}",userId,status);
        return userService.findCouponsByStatus(userId,status);
    }

    /**
     * 根据用户id查询当前可以领取的优惠券模板
     * 127.0.0.1:7002/coupon-distribution/template
     * 127.0.0.1:9000/zeroclian/coupon-distribution/template
     */
    @GetMapping("/template")
    public List<CouponTemplateSDK> findAvailableTemplate(@RequestParam("userId") Long userId)
            throws CouponException{
        log.info("Find Available Template: {}.",userId);
        return userService.findAvailableTemplate(userId);
    }

    /**
     * 用户领取优惠券
     * 127.0.0.1:7002/coupon-distribution/acquire/template
     * 127.0.0.1:9000/zeroclian/coupon-distribution/acquire/template
     */
    @PostMapping("/acquire/template")
    public Coupon acquireTemplate(@RequestBody AcquireTemplateRequest request) throws CouponException{
        log.info("acquire Template:{}", JSON.toJSONString(request));
        return userService.acquireTemplate(request);
    }

    /**
     *结算（核销）优惠券
     * 127.0.0.1:7002/coupon-distribution/settlement
     * 127.0.0.1:9000/zeroclian/coupon-distribution/settlement
     */
    @PostMapping("/settlement")
    public SettlementInfo settlement(@RequestBody SettlementInfo info) throws CouponException{
        log.info("Settlement: {}",JSON.toJSONString(info));
        return userService.settlement(info);
    }
}

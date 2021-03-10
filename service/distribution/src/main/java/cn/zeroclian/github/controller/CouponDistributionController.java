package cn.zeroclian.github.controller;

import cn.zeroclian.github.entity.Coupon;
import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.service.IUserService;

import cn.zeroclian.github.vo.AcquireTemplateRequest;
import cn.zeroclian.github.vo.CouponTemplateSDK;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Desciption  优惠券分发相关的功能控制器
 * @Author ZeroClian
 * @Date 2021-03-09-15:53
 */
@Slf4j
@RestController
public class CouponDistributionController {
    private final IUserService userService;
    @Autowired
    public CouponDistributionController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 根据用户id和状态查询优惠券记录
     */
    @GetMapping("/distribution/findCouponsByStatus")
    public List<Coupon> findCouponsByStatus(@RequestParam("userId") Long userId,
                                            @RequestParam("status") Integer status) throws CouponException{
        log.info("{} find Coupons By Status: {}",userId,status);
        return userService.findCouponsByStatus(userId,status);
    }

    /**
     * 根据用户id查询当前可以领取的优惠券模板
     */
    @GetMapping("/distribution/findAvailableTemplate")
    public List<CouponTemplateSDK> findAvailableTemplate(@RequestParam("userId") Long userId)
            throws CouponException{
        log.info("find Available Template.");
        return userService.findAvailableTemplate(userId);
    }

    /**
     * 用户领取优惠券
     */
    @GetMapping("/distribution/acquireTemplate")
    public Coupon acquireTemplate(@RequestBody AcquireTemplateRequest request) throws CouponException{
        log.info("acquire Template:{}", JSON.toJSONString(request));
        return userService.acquireTemplate(request);
    }

}

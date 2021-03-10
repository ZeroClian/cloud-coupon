package cn.zeroclian.github.service;

import cn.zeroclian.github.entity.Coupon;
import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.vo.AcquireTemplateRequest;
import cn.zeroclian.github.vo.CouponTemplateSDK;
import cn.zeroclian.github.vo.SettlementInfo;

import java.util.List;

/**
 * @Desciption 用户服务相关接口定义
 * 1. 用户三类状态优惠券信息展示服务
 * 2. 查看用户当前可以领取的优惠券模板  - template微服务配合实现
 * 3. 用户领取优惠券服务
 * 4. 用户消费优惠券服务 - settlement微服务配合实现
 * @Author ZeroClian
 * @Date 2021-03-03-15:35
 */
public interface IUserService {
    /**
     * 根据用户id和状态查询优惠券记录
     *
     * @param userId 用户id
     * @param status 优惠券状态
     * @return {@link Coupon}s
     * @throws CouponException
     */
    List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException;

    /**
     * 根据用户id查询当前可以领取的优惠券模板
     *
     * @param userId 用户id
     * @return {@link CouponTemplateSDK}s
     * @throws CouponException
     */
    List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException;

    /**
     * 用户领取优惠券
     *
     * @param request {@link AcquireTemplateRequest}
     * @return {@link Coupon}
     * @throws CouponException
     */
    Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException;

    /**
     * 结算（核销）优惠券
     * @param info  {@link SettlementInfo}
     * @return  {@link SettlementInfo}
     * @throws CouponException
     */
    SettlementInfo settlement(SettlementInfo info) throws CouponException;
}

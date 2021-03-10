package cn.zeroclian.github.service;

import cn.zeroclian.github.entity.Coupon;
import cn.zeroclian.github.exception.CouponException;

import java.util.List;

/**
 * @Desciption Redis相关的操作服务接口定义
 * 1. 用户的三个状态优惠券 Cache 操作
 * 2. 优惠券模板生成的优惠券码 Cache 操作
 * @Author ZeroClian
 * @Date 2021-03-02-20:46
 */
public interface IRedisService {

    /**
     * <h2>根据用户id和状态找到缓存的优惠券列表数据</h2>
     *
     * @param userId 用户id
     * @param status 优惠券状态
     * @return {@link Coupon}可能会返回null，代表没有过记录
     */
    List<Coupon> getCachedCoupons(Long userId, Integer status);

    /**
     * 保存空的优惠券列表到缓存中
     *
     * @param userId 用户id
     * @param status 优惠券状态列表
     */
    void saveEmptyCouponListToCache(Long userId, List<Integer> status);

    /**
     * 尝试从 Cache 中获取一个优惠券码
     *
     * @param templateId 优惠券模板id
     * @return 优惠券码
     */
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    /**
     * 将优惠券保存到Cache中
     * @param userId    用户id
     * @param coupons   优惠券
     * @param status    优惠券状态
     * @return  保存个数
     * @throws CouponException
     */
    Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException;
}

package cn.zeroclian.github.service;

import cn.zeroclian.github.entity.CouponTemplate;

/**
 * @Desciption  异步服务接口定义
 * @Author ZeroClian
 * @Date 2021-02-26-15:50
 */
public interface IAsyncService {

    /**
     * <h2>根据模板异步的创建优惠券码</h2>
     * @param template {@link CouponTemplate} 优惠券模板实体
     * */
    void asyncConstructCouponByTemplate(CouponTemplate template);
}

package cn.zeroclian.github.dao;

import cn.zeroclian.github.constant.CouponStatus;
import cn.zeroclian.github.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Desciption  Coupon Dao 接口定义
 * @Author ZeroClian
 * @Date 2021-03-02-20:38
 */
public interface CouponDao extends JpaRepository<Coupon,Integer> {

    /**
     * 根据用户id+状态查询优惠券
     * @param userId    用户id
     * @param status    状态
     * @return
     */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);

    /**
     * 根据 userId 寻找优惠券记录
     * @param userId    用户id
     * @return
     */
    List<Coupon> findAllByUserId(Long userId);
}

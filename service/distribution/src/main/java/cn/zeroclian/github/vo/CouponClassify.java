package cn.zeroclian.github.vo;

import cn.zeroclian.github.constant.CouponStatus;
import cn.zeroclian.github.constant.PeriodType;
import cn.zeroclian.github.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Desciption  根据优惠券的状态将用户优惠券进行分类
 * @Author ZeroClian
 * @Date 2021-03-04-15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponClassify {
    /**
     * 可以使用的
     */
    private List<Coupon> usable;
    /**
     * 已使用的
     */
    private List<Coupon> used;
    /**
     * 已过期的
     */
    private List<Coupon> expired;

    /**
     * 对当前的优惠券进行分类
     */
    public static CouponClassify classify(List<Coupon> coupons) {
        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());
        coupons.forEach(coupon -> {

            //判断优惠券是否过期
            boolean isTimeExpire;
            long curTime = new Date().getTime();

            if (coupon.getTemplateSDK().getRule().getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())) {
                isTimeExpire = coupon.getTemplateSDK().getRule().getExpiration().getDeadline() <= curTime;
            } else {
                isTimeExpire = DateUtils.addDays(
                        coupon.getAssignTime(),
                        coupon.getTemplateSDK().getRule().getExpiration().getGap()
                ).getTime() <= curTime;
            }
            if (coupon.getStatus() == CouponStatus.USED) {
                used.add(coupon);
            } else if (coupon.getStatus() == CouponStatus.EXPIRED || isTimeExpire) {
                expired.add(coupon);
            } else {
                usable.add(coupon);
            }
        });

        return new CouponClassify(usable, used, expired);
    }
}

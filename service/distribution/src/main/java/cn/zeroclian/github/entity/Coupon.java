package cn.zeroclian.github.entity;

import cn.zeroclian.github.constant.CouponStatus;
import cn.zeroclian.github.converter.CouponStatusConverter;
import cn.zeroclian.github.serialization.CouponSerialize;
import cn.zeroclian.github.vo.CouponTemplateSDK;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @Desciption  优惠券（用户领取的优惠券记录）实体表
 * @Author ZeroClian
 * @Date 2021-03-01-21:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon")
@JsonSerialize(using = CouponSerialize.class)
public class Coupon {
    /** 自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;

    /** 关联优惠券模板的主键（逻辑外键）*/
    @Column(name = "template_id",nullable = false)
    private Integer templateId;

    /** 领取用户*/
    @Column(name = "user_id",nullable = false)
    private Long userId;

    /** 优惠券码*/
    @Column(name = "coupon_code",nullable = false)
    private String couponCode;

    /** 领取时间*/
    @CreatedDate
    @Column(name = "assign_time",nullable = false)
    private Date assignTime;

    /** 优惠券状态*/
    @Column(name = "status",nullable = false)
    @Convert(converter = CouponStatusConverter.class)
    private CouponStatus status;

    /** 用户优惠券对应的模板信息*/
    @Transient
    private CouponTemplateSDK templateSDK;

    /**
     * 返回一个无效的Coupon对象
     * @return
     */
    public static Coupon invalidCoupon(){
        Coupon coupon = new Coupon();
        coupon.setId(-1);
        return coupon;
    }

    /**
     * 构造优惠券
     * @param templateId
     * @param userId
     * @param couponCode
     * @param status
     */
    public Coupon(Integer templateId, Long userId, String couponCode, CouponStatus status) {
        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = status;
    }
}

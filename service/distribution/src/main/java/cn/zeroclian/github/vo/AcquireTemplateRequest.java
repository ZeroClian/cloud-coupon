package cn.zeroclian.github.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Desciption  获取优惠券请求对象定义
 * @Author ZeroClian
 * @Date 2021-03-03-15:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcquireTemplateRequest {

    /** 用户id */
    private Long userId;

    /** 优惠券模板信息*/
    private CouponTemplateSDK templateSDK;
}

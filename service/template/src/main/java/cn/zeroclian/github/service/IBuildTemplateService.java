package cn.zeroclian.github.service;

import cn.zeroclian.github.entity.CouponTemplate;
import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.vo.TemplateRequest;

/**
 * @Desciption  构建优惠券模板接口定义
 * @Author ZeroClian
 * @Date 2021-02-26-15:45
 */
public interface IBuildTemplateService {

    /**
     * <h2>创建优惠券模板</h2>
     * @param request {@link TemplateRequest} 模板信息请求对象
     * @return {@link CouponTemplate} 优惠券模板实体
     * */
    CouponTemplate buildTemplate(TemplateRequest request)
            throws CouponException;

}

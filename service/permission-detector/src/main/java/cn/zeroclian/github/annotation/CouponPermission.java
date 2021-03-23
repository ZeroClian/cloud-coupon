package cn.zeroclian.github.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Desciption  权限描述注解：定义 Controller 接口的权限
 * @Author ZeroClian
 * @Date 2021-03-23-15:51
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CouponPermission {

    /**
     * 接口描述信息
     */
    String description() default "";

    /**
     * 此接口是否为只读，默认是 true
     */
    boolean readOnly() default true;

    /**
     * 扩展属性
     * 最好以 JSON 格式去存储
     */
    String extra() default "";

}

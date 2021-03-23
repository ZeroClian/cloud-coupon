package cn.zeroclian.github.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Desciption  权限忽略注解：忽略当前表示的 Controller 接口，不注册权限
 * @Author ZeroClian
 * @Date 2021-03-23-15:55
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnorePermission {
}

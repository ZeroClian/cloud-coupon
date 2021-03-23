package cn.zeroclian.github.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Desciption  用户角色枚举
 * @Author ZeroClian
 * @Date 2021-03-23-14:48
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    ADMIN("管理员"),
    SUPER_ADMIN("超级管理员"),
    CUSTOMER("普通用户");

    private String roleName;
}

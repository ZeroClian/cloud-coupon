package cn.zeroclian.github;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Desciption  操作模式的枚举定义
 * @Author ZeroClian
 * @Date 2021-03-23-11:21
 */
@Getter
@AllArgsConstructor
public enum OpModeEnum {

    READ("读"),
    WRITE("写");

    private String mode;

}

package cn.zeroclian.github.vo;

import lombok.Data;

/**
 * @Desciption  接口权限信息组装类定义
 * @Author ZeroClian
 * @Date 2021-03-23-16:01
 */
@Data
public class PermissionInfo {

    /** Controller 的 URL */
    private String url;

    /** 方法类型 */
    private String method;

    /** 是否是只读的 */
    private Boolean isRead;

    /** 方法描述信息 */
    private String description;

    /** 扩展属性 */
    private String extra;

    @Override
    public String toString() {
        return "PermissionInfo{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", isRead=" + isRead +
                ", description='" + description + '\'' +
                '}';
    }
}

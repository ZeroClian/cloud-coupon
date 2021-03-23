package cn.zeroclian.github.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Desciption  权限校验请求对象定义
 * @Author ZeroClian
 * @Date 2021-03-23-11:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPermissionRequest {

    private Long userId;

    private String uri;

    private String httpMethod;


}

package cn.zeroclian.github.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Desciption  路径创建请求对象定义
 * @Author ZeroClian
 * @Date 2021-03-23-11:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePathRequest {

    private List<PathInfo> pathInfos;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PathInfo{

        // 路径模式
        private String pathPattern;

        //Http 方法类型
        private String httpMethod;

        //路径名称
        private String pathName;

        //服务名称
        private String serviceName;

        //操作模式：读 or 写
        private String opMode;
    }
}

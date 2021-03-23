package cn.zeroclian.github;

import cn.zeroclian.github.permission.PermissionClient;
import cn.zeroclian.github.vo.CommonResponse;
import cn.zeroclian.github.vo.CreatePathRequest;
import cn.zeroclian.github.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Desciption 权限注册组件
 * @Author ZeroClian
 * @Date 2021-03-23-17:01
 */
@Slf4j
public class PermissionRegistry {

    /**
     * 权限服务 SDK 客户端
     */
    private PermissionClient permissionClient;

    /**
     * 服务名称
     */
    private String serviceName;

    public PermissionRegistry(PermissionClient permissionClient, String serviceName) {
        this.permissionClient = permissionClient;
        this.serviceName = serviceName;
    }

    /**
     * 权限注册
     */
    boolean register(List<PermissionInfo> infoList) {
        if (CollectionUtils.isEmpty(infoList)) {
            return false;
        }
        List<CreatePathRequest.PathInfo> pathInfos = infoList.stream()
                .map(info -> CreatePathRequest.PathInfo.builder()
                        .pathPattern(info.getUrl())
                        .httpMethod(info.getMethod())
                        .pathName(info.getDescription())
                        .serviceName(serviceName)
                        .opMode(info.getIsRead() ?
                                OpModeEnum.READ.name() : OpModeEnum.WRITE.name())
                        .build()
                ).collect(Collectors.toList());
        CommonResponse<List<Integer>> response = permissionClient.createPath(
                new CreatePathRequest(pathInfos)
        );
        if (!CollectionUtils.isEmpty(response.getData())){
            log.info("register path info: {}",response.getData());
            return true;
        }
        return false;
    }
}

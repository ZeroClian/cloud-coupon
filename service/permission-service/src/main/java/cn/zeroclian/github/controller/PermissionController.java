package cn.zeroclian.github.controller;

import cn.zeroclian.github.annotation.IgnoreResponseAdvice;
import cn.zeroclian.github.service.PathService;
import cn.zeroclian.github.service.PermissionService;
import cn.zeroclian.github.vo.CheckPermissionRequest;
import cn.zeroclian.github.vo.CreatePathRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Desciption  路径创建与权限校验对外服务接口实现
 * @Author ZeroClian
 * @Date 2021-03-23-15:20
 */
@Slf4j
@RestController
public class PermissionController {

    private final PathService pathService;

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PathService pathService, PermissionService permissionService) {
        this.pathService = pathService;
        this.permissionService = permissionService;
    }

    /**
     * 路径创建接口
     */
    @PostMapping("/create/path")
    public List<Integer> createPath(@RequestBody CreatePathRequest request){
        log.info("createPath: {}",request.getPathInfos().size());
        return pathService.createPath(request);
    }

    /**
     * 权限校验接口
     */
    @IgnoreResponseAdvice
    @PostMapping("/check/permission")
    public Boolean checkPermission(@RequestBody CheckPermissionRequest request) {
        log.info("checkPermission for args: {}, {}, {}",
                request.getUserId(),request.getUri(),request.getHttpMethod());
        return permissionService.checkPermission(
                request.getUserId(),request.getUri(),request.getHttpMethod());
    }
}

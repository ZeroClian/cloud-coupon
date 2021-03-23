package cn.zeroclian.github;

import cn.zeroclian.github.permission.PermissionClient;
import cn.zeroclian.github.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

/**
 * @Desciption 权限探测监听器，Spring 容器启动后自动运行
 * @Author ZeroClian
 * @Date 2021-03-23-17:13
 */
@Slf4j
@Component
public class PermissionDetectListener implements
        ApplicationListener<ApplicationReadyEvent> {

    private static final String KEY_SERVER_CTX = "server.servlet.context-path";

    private static final String KEY_SERVICE_NAME = "spring.application.name";

    @Override
    @SuppressWarnings("all")
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        ApplicationContext ctx = applicationReadyEvent.getApplicationContext();
        new Thread(() -> {

            //扫描权限（注解）
            List<PermissionInfo> infoList = scanPermission(ctx);

            //注册权限
            registerPermission(infoList, ctx);

        }).start();
    }

    /**
     * 注册接口权限
     *
     * @param infoList
     * @param ctx
     */
    private void registerPermission(List<PermissionInfo> infoList,
                                    ApplicationContext ctx) {
        log.info("*************** register permission ****************");

        PermissionClient permissionClient = ctx.getBean(PermissionClient.class);
        if (null == permissionClient) {
            log.error("no permissionClient bean found");
            return;
        }
        //取出 Service Name
        String serviceName = ctx.getEnvironment().getProperty(KEY_SERVICE_NAME);
        log.info("serviceName: {}", serviceName);
        boolean result = new PermissionRegistry(permissionClient, serviceName)
                .register(infoList);
        if (result) {
            log.info("*************** done register ****************");
        }
    }

    /**
     * 扫描微服务中的 Controller 的接口权限信息
     *
     * @param ctx
     * @return
     */
    private List<PermissionInfo> scanPermission(ApplicationContext ctx) {
        //取出 context 前缀
        String pathPrefix = ctx.getEnvironment().getProperty(KEY_SERVER_CTX);

        //取出 Spring 的映射 bean
        RequestMappingHandlerMapping mappingBean =
                (RequestMappingHandlerMapping) ctx.getBean("RequestMappingHandlerMapping");
        //扫描权限
        List<PermissionInfo> permissionInfoList = new AnnotationScanner(pathPrefix)
                .scanPermission(mappingBean.getHandlerMethods());

        permissionInfoList.forEach(p -> log.info("{}", p));
        log.info("{} permission found", permissionInfoList.size());
        log.info("*************** done scanning ****************");
        return permissionInfoList;
    }
}

package cn.zeroclian.github;

import cn.zeroclian.github.annotation.CouponPermission;
import cn.zeroclian.github.annotation.IgnorePermission;
import cn.zeroclian.github.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @Desciption 接口权限信息扫描器
 * @Author ZeroClian
 * @Date 2021-03-23-16:08
 */
@Slf4j
public class AnnotationScanner {

    private String pathPrefix;

    private static final String COUPON_PKG = "cn.zeroclian.github";

    AnnotationScanner(String prefix) {
        this.pathPrefix = trimPath(prefix);
    }

    /**
     * 构造所有 Controller 的权限信息
     */
    List<PermissionInfo> scanPermission(Map<RequestMappingInfo, HandlerMethod> mappingMap) {
        List<PermissionInfo> result = new ArrayList<>();
        mappingMap.forEach((mapInfo, method) ->
                result.addAll(buildPermission(mapInfo, method)));
        return result;
    }

    /**
     * 构造 Controller 的权限信息
     *
     * @param mapInfo       {@link RequestMappingInfo} @RequestMapping 对应的信息
     * @param handlerMethod {@link HandlerMethod}   @RequestMapping 对应方法的详情信息，包括方法、类、参数
     * @return
     */
    private List<PermissionInfo> buildPermission(RequestMappingInfo mapInfo,
                                                 HandlerMethod handlerMethod) {
        Method javaMethod = handlerMethod.getMethod();
        Class baseClass = javaMethod.getDeclaringClass();

        //忽略掉不是 cn.zeroclian.github 下的mapping
        if (!isCouponPackage(baseClass.getName())) {
            log.debug("ignore method: {}", javaMethod.getName());
            return Collections.emptyList();
        }
        //判断是否需要忽略此方法
        IgnorePermission ignorePermission = javaMethod.getAnnotation(IgnorePermission.class);
        if (null != ignorePermission) {
            log.debug("ignore method: {}", javaMethod.getName());
            return Collections.emptyList();
        }
        //取出权限注解
        CouponPermission couponPermission = javaMethod.getAnnotation(CouponPermission.class);
        if (null == couponPermission) {
            //如果没有标注 IgnorePermission，且没有 CouponPermission
            log.error("lack CouponPermission -> {}#{}",
                    javaMethod.getDeclaringClass().getName(),
                    javaMethod.getName());
            return Collections.emptyList();
        }
        //取出 URL
        Set<String> urlSet = mapInfo.getPatternsCondition().getPatterns();
        //取出 method
        boolean isAllMethods = false;
        Set<RequestMethod> methodSet = mapInfo.getMethodsCondition().getMethods();
        if (CollectionUtils.isEmpty(methodSet)) {
            isAllMethods = true;
        }

        List<PermissionInfo> infoList = new ArrayList<>();
        for (String url : urlSet) {

            //支持的 http method 为全量
            if (isAllMethods) {
                PermissionInfo info = buildPermissionInfo(
                        HttpMethodEnum.ALL.name(),
                        javaMethod.getName(),
                        this.pathPrefix + url,
                        couponPermission.readOnly(),
                        couponPermission.description(),
                        couponPermission.extra()
                );
                infoList.add(info);
                continue;
            }

            //支持部分 http method
            for (RequestMethod method : methodSet) {
                PermissionInfo info = buildPermissionInfo(
                        method.name(),
                        javaMethod.getName(),
                        this.pathPrefix + url,
                        couponPermission.readOnly(),
                        couponPermission.description(),
                        couponPermission.extra()
                );
                infoList.add(info);
                log.info("permission detected: {}", info);
            }
        }
        return infoList;


    }

    /**
     * 构造单个接口的权限信息
     */
    private PermissionInfo buildPermissionInfo(String reqMethod, String javaMethod, String path,
                                               boolean readOnlg, String desc, String extra) {
        PermissionInfo info = new PermissionInfo();
        info.setMethod(reqMethod);
        info.setUrl(path);
        info.setIsRead(readOnlg);
        info.setDescription(StringUtils.isEmpty(desc) ? javaMethod : desc);
        info.setExtra(extra);
        return info;
    }

    /**
     * 判断当前类是否是在定义的包中
     *
     * @param className 类名
     * @return
     */
    private boolean isCouponPackage(String className) {
        return className.startsWith(COUPON_PKG);
    }

    /**
     * 保证 path 以 / 开头，且不以 / 结尾
     *
     * @return user -> /user, /user/ -> /user
     */
    private String trimPath(String path) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

}

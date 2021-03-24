package cn.zeroclian.github.filter;

import cn.zeroclian.github.permission.PermissionClient;
import cn.zeroclian.github.vo.CheckPermissionRequest;
import cn.zeroclian.github.vo.CommonResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Desciption 权限过滤器实现
 * @Author ZeroClian
 * @Date 2021-03-23-18:01
 */
@Slf4j
@Component
public class PermissionFilter extends AbsSecurityFilter {

    private final PermissionClient permissionClient;

    @Autowired
    public PermissionFilter(PermissionClient permissionClient) {
        this.permissionClient = permissionClient;
    }

    @Override
    protected Boolean interceptCheck(HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        //执行权限校验的逻辑
        //从 Header 中获取到 userId
        Long userId = Long.valueOf(request.getHeader("userId"));
        String uri = request.getRequestURI().substring("/zeroclian".length());
        String httpMethod = request.getMethod();

        return permissionClient.checkPermission(
                new CheckPermissionRequest(userId, uri, httpMethod));
    }

    @Override
    protected int getHttpStatus() {
        return HttpStatus.OK.value();
    }

    @Override
    protected String getErrorMsg() {
        CommonResponse<Object> response = new CommonResponse<>();
        response.setCode(-2);
        response.setMessage("您没有操作权限！！！");

        return JSON.toJSONString(response);
    }
}

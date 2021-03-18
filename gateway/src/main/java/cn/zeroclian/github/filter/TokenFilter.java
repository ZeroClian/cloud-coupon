package cn.zeroclian.github.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Desciption 自定义校验请求中的token
 * @Author ZeroClian
 * @Date 2021-02-25-20:07
 */
@Slf4j
//@Component
public class TokenFilter extends AbstractPreZuulFilter {
    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        log.info(String.format("%s request to %s",
                request.getMethod(),request.getRequestURL().toString()));
        Object token = request.getParameter("token");
        if ( null == token){
            log.error("error: token is empty!");
            return fail(401,"error: token is empty!");
        }
        return success();
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}

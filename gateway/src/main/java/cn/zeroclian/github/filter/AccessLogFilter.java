package cn.zeroclian.github.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Desciption  自定义日志
 * @Author ZeroClian
 * @Date 2021-02-25-20:19
 */
@Slf4j
@Component
public class AccessLogFilter extends AbstractPostZuulFilter {
    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        Long startTime = (Long) context.get("startTime");
        String uri = request.getRequestURI();
        Long duration = System.currentTimeMillis() - startTime;
        // 从网关通过的请求都会打印日志记录: uri + duration
        log.info("uri: {}, duration: {}",uri,duration);
        return success();
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER-1;
    }
}

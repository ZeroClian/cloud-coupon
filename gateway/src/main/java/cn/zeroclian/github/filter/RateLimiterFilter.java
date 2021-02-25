package cn.zeroclian.github.filter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Desciption  限流过滤器
 * @Author ZeroClian
 * @Date 2021-02-25-20:12
 */
@Slf4j
@Component
public class RateLimiterFilter extends AbstractPreZuulFilter {

    //每秒可以获取2个令牌
    RateLimiter rateLimiter = RateLimiter.create(2.0);

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        if (rateLimiter.tryAcquire()){
            log.info("get rate token success!");
            return success();
        }else {
            log.error("rate limit: {}",request.getRequestURI());
            return fail(402,"error: rate limit!");
        }
    }

    @Override
    public int filterOrder() {
        return 2;
    }
}

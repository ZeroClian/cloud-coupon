package cn.zeroclian.github.filter;

/**
 * @Desciption  在过滤器中存储客户端发起请求的时间戳
 * @Author ZeroClian
 * @Date 2021-02-25-20:17
 */
public class PreRequestFilter extends AbstractPreZuulFilter {
    @Override
    protected Object cRun() {
        context.set("startTime",System.currentTimeMillis());
        return success();
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}

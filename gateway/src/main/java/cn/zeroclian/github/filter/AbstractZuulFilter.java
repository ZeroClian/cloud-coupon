package cn.zeroclian.github.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;


/**
 * @Desciption  通用的抽象过滤器类
 * @Author ZeroClian
 * @Date 2021-02-25-19:48
 */
public abstract class AbstractZuulFilter extends ZuulFilter {

    // 用于在过滤器之间传递消息, 数据保存在每个请求的 ThreadLocal 中
    // 扩展了 Map
    RequestContext context;

    //标志
    private final static String NEXT = "next";

    /**
     * 是否执行过滤器的run方法
     * true：执行；false：不执行
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (boolean) ctx.getOrDefault(NEXT,true);
    }

    /**
     * 过滤逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
         context = RequestContext.getCurrentContext();
        return cRun();
    }

    protected abstract Object cRun();

    /**
     * 失败响应
     * @param code
     * @param msg
     * @return
     */
    Object fail(int code,String msg){
        context.set(NEXT,false);
        context.setSendZuulResponse(false);
        context.getResponse().setContentType("text/html;charset=UTF-8");
        context.setResponseStatusCode(code);
        context.setResponseBody(String.format("{\"result\": \"%s!\",msg}"));
        return null;
    }

    /**
     * 成功响应
     * @return
     */
    Object success(){
        context.set(NEXT,true);
        return null;
    }
}

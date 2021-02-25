package cn.zeroclian.github.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.FilterType;

/**
 * @Desciption
 * @Author ZeroClian
 * @Date 2021-02-25-20:03
 */
public abstract class AbstractPreZuulFilter extends AbstractZuulFilter{
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
}

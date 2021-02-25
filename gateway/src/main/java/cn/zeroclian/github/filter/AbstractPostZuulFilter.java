package cn.zeroclian.github.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * @Desciption
 * @Author ZeroClian
 * @Date 2021-02-25-20:05
 */
public abstract class AbstractPostZuulFilter extends AbstractZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }
}

package cn.zeroclian.github.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Desciption  定制 HTTP 消息转换器
 * @Author ZeroClian
 * @Date 2021-02-25-20:43
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        //添加转换器
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}

package cn.zeroclian.github.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @Desciption  Jackson的自定义配置
 * @Author ZeroClian
 * @Date 2021-02-25-20:57
 */
@Configuration
@SuppressWarnings("all")
public class JacksonConfig {
    @Bean
    public ObjectMapper getObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:ss:mm"));
        return mapper;
    }
}

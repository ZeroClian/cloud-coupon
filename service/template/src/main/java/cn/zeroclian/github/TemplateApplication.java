package cn.zeroclian.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Desciption  模板微服务的启动入口
 * @Author ZeroClian
 * @Date 2021-02-26-14:49
 */
@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
@EnableJpaAuditing
public class TemplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class,args);
    }
}

package cn.zeroclian.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Desciption 优惠券结算微服务的启动入口
 * @Author ZeroClian
 * @Date 2021-03-10-14:05
 */
@SpringBootApplication
@EnableEurekaClient
public class SettlementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SettlementApplication.class, args);
    }
}

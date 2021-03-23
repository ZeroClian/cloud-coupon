package cn.zeroclian.github;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Desciption  网关应用启动入口
 * @Author ZeroClian
 * @Date 2021-02-25-19:46
 */
@SpringCloudApplication
@EnableZuulProxy
@EnableFeignClients
public class ZuulGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class,args);
    }
}

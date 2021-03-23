package cn.zeroclian.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Desciption  权限服务启动程序
 * @Author ZeroClian
 * @Date 2021-03-23-11:49
 */
@SpringBootApplication
@EnableEurekaClient
public class PermissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class,args);
    }
}

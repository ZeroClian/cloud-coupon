package cn.zeroclian.github.controller;

import cn.zeroclian.github.annotation.IgnoreResponseAdvice;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Desciption  Ribbon 应用 Controller
 * @Author ZeroClian
 * @Date 2021-03-22-9:04
 */
@Slf4j
@RestController
public class RibbonController {
    /** rest 客户端 */
    private final RestTemplate restTemplate;

    @Autowired
    public RibbonController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 通过 Ribbon 组件调用模板微服务
     * /coupon-distribution/info
     */
    @GetMapping("/info")
    @IgnoreResponseAdvice
    public TemplateInfo getTemplateInfo(){
        String infoUrl = "http://eureka-client-coupon-template/coupon-template/info";
        return restTemplate.getForEntity(infoUrl,TemplateInfo.class).getBody();
    }
    /**
     * 模板微服务的元信息
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class TemplateInfo{
        private Integer code;
        private String message;
        private List<Map<String,Object>> data;
    }
}

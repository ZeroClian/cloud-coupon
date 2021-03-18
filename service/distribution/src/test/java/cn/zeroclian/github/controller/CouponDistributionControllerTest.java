package cn.zeroclian.github.controller;

import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.service.IUserService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class CouponDistributionControllerTest {

    private Long fakeUserId = 20001L;

    @Autowired
    private IUserService userService;

    @Test
    public void findCouponsByStatus() {
    }

    @Test
    public void findAvailableTemplate() throws CouponException {
        System.out.println(JSON.toJSONString(
                userService.findAvailableTemplate(fakeUserId)
        ));
    }

    @Test
    public void acquireTemplate() {
    }
}
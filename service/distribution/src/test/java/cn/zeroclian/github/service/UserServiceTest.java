package cn.zeroclian.github.service;

import cn.zeroclian.github.constant.CouponStatus;
import cn.zeroclian.github.exception.CouponException;
import com.alibaba.fastjson.JSON;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Desciption 用户服务功能测试用例
 * @Author ZeroClian
 * @Date 2021-03-22-15:17
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    private Long fakeUserId = 20001L;
    @Autowired
    private IUserService userService;

    @Test
    public void testFindCouponByStatus() throws CouponException {
        System.out.println(JSON.toJSONString(
                userService.findCouponsByStatus(fakeUserId, CouponStatus.USABLE.getCode())
        ));
    }

    @Test
    public void testFindAvailableTemplate() throws CouponException{
        System.out.println(JSON.toJSONString(
                userService.findAvailableTemplate(fakeUserId)
        ));
    }
}

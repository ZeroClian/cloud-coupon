package cn.zeroclian.github.service;

import cn.zeroclian.github.constant.CouponCategory;
import cn.zeroclian.github.constant.GoodsType;
import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.executor.ExecuteManager;
import cn.zeroclian.github.vo.CouponTemplateSDK;
import cn.zeroclian.github.vo.GoodsInfo;
import cn.zeroclian.github.vo.SettlementInfo;
import cn.zeroclian.github.vo.TemplateRule;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Desciption 结算规则执行管理器测试用例
 * @Author ZeroClian
 * @Date 2021-03-22-16:16
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ExecuteManagerTest {

    private Long fakeUserId = 20001L;
    @Autowired
    private ExecuteManager manager;

    @Test
    public void testComputeRule() throws CouponException {
        //满减优惠券结算测试
//        log.info("ManJian Coupon Executor Test");
//        SettlementInfo manJianInfo = fakeManJianCouponSettlement();
//        SettlementInfo result = manager.computeRule(manJianInfo);
        //折扣优惠券结算测试
//        log.info("ZheKou Coupon Executor Test");
//        SettlementInfo zheKouInfo = fakeZheKouCouponSettlement();
//        SettlementInfo result = manager.computeRule(zheKouInfo);
        //立减优惠券结算测试
//        log.info("LiJian Coupon Executor Test");
//        SettlementInfo liJianInfo = fakeLiJianCouponSettlement();
//        SettlementInfo result = manager.computeRule(liJianInfo);
        //满减+折扣优惠券结算测试
        log.info("ManJian ZheKou Coupon Executor Test");
        SettlementInfo manjianZheKouInfo = fakeManJianAndZheKouCouponSettlement();
        SettlementInfo result = manager.computeRule(manjianZheKouInfo);

        log.info("{}", result.getCost());
        log.info("{}", result.getCouponAndTemplateInfos().size());
        log.info("{}", result.getCouponAndTemplateInfos());
    }

    /**
     * fake(mock) 满减优惠卷结算信息
     */
    private SettlementInfo fakeManJianCouponSettlement() {
        SettlementInfo info = new SettlementInfo();
        info.setUserId(fakeUserId);
        info.setEmploy(false);
        info.setCost(0.0);

        GoodsInfo goodsInfo01 = new GoodsInfo();
        goodsInfo01.setCount(2);
        goodsInfo01.setPrice(10.88);
        goodsInfo01.setType(GoodsType.WENYU.getCode());

        GoodsInfo goodsInfo02 = new GoodsInfo();
//        goodsInfo02.setCount(10);
        goodsInfo02.setCount(5);
        goodsInfo02.setPrice(20.88);
        goodsInfo02.setType(GoodsType.WENYU.getCode());

        info.setGoodsInfos(Arrays.asList(goodsInfo01, goodsInfo02));

        SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();
        ctInfo.setId(1);

        CouponTemplateSDK templateSDK = new CouponTemplateSDK();
        templateSDK.setId(1);
        templateSDK.setCategory(CouponCategory.MANJIAN.getCode());
        templateSDK.setKey("100120210322");

        TemplateRule rule = new TemplateRule();
        rule.setDiscount(new TemplateRule.Discount(20, 199));
        rule.setUsage(new TemplateRule.Usage("广东省", "广州市",
                JSON.toJSONString(Arrays.asList(GoodsType.WENYU.getCode(), GoodsType.JIAJU.getCode()))));

        templateSDK.setRule(rule);
        ctInfo.setTemplate(templateSDK);
        info.setCouponAndTemplateInfos(Collections.singletonList(ctInfo));
        return info;

    }

    /**
     * fake(mock) 折扣优惠卷结算信息
     */
    private SettlementInfo fakeZheKouCouponSettlement(){

        SettlementInfo info = new SettlementInfo();
        info.setUserId(fakeUserId);
        info.setEmploy(false);
        info.setCost(0.0);

        GoodsInfo goodsInfo01 = new GoodsInfo();
        goodsInfo01.setCount(2);
        goodsInfo01.setPrice(10.88);
        goodsInfo01.setType(GoodsType.WENYU.getCode());

        GoodsInfo goodsInfo02 = new GoodsInfo();
        goodsInfo02.setCount(10);
        goodsInfo02.setPrice(20.88);
        goodsInfo02.setType(GoodsType.WENYU.getCode());

        info.setGoodsInfos(Arrays.asList(goodsInfo01, goodsInfo02));

        SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();
        ctInfo.setId(1);

        CouponTemplateSDK templateSDK = new CouponTemplateSDK();
        templateSDK.setId(2);
        templateSDK.setCategory(CouponCategory.ZHEKOU.getCode());
        templateSDK.setKey("100120210323");

        TemplateRule rule = new TemplateRule();
        rule.setDiscount(new TemplateRule.Discount(85, 1));
        rule.setUsage(new TemplateRule.Usage("广东省", "广州市",
                JSON.toJSONString(Arrays.asList(GoodsType.WENYU.getCode(), GoodsType.JIAJU.getCode()))));

        templateSDK.setRule(rule);
        ctInfo.setTemplate(templateSDK);
        info.setCouponAndTemplateInfos(Collections.singletonList(ctInfo));
        return info;
    }

    /**
     * fake(mock) 立减优惠卷结算信息
     */
    private SettlementInfo fakeLiJianCouponSettlement(){

        SettlementInfo info = new SettlementInfo();
        info.setUserId(fakeUserId);
        info.setEmploy(false);
        info.setCost(0.0);

        GoodsInfo goodsInfo01 = new GoodsInfo();
        goodsInfo01.setCount(2);
        goodsInfo01.setPrice(10.88);
        goodsInfo01.setType(GoodsType.WENYU.getCode());

        GoodsInfo goodsInfo02 = new GoodsInfo();
        goodsInfo02.setCount(10);
        goodsInfo02.setPrice(20.88);
        goodsInfo02.setType(GoodsType.WENYU.getCode());

        info.setGoodsInfos(Arrays.asList(goodsInfo01, goodsInfo02));

        SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();
        ctInfo.setId(3);

        CouponTemplateSDK templateSDK = new CouponTemplateSDK();
        templateSDK.setId(3);
        templateSDK.setCategory(CouponCategory.LIJIAN.getCode());
        templateSDK.setKey("100120210324");

        TemplateRule rule = new TemplateRule();
        rule.setDiscount(new TemplateRule.Discount(15, 1));
        rule.setUsage(new TemplateRule.Usage("广东省", "广州市",
                JSON.toJSONString(Arrays.asList(GoodsType.WENYU.getCode(), GoodsType.JIAJU.getCode()))));

        templateSDK.setRule(rule);
        ctInfo.setTemplate(templateSDK);
        info.setCouponAndTemplateInfos(Collections.singletonList(ctInfo));
        return info;
    }

    /**
     * fake(mock) 满减+折扣优惠卷结算信息
     */
    private SettlementInfo fakeManJianAndZheKouCouponSettlement() {
        SettlementInfo info = new SettlementInfo();
        info.setUserId(fakeUserId);
        info.setEmploy(false);
        info.setCost(0.0);

        GoodsInfo goodsInfo01 = new GoodsInfo();
        goodsInfo01.setCount(2);
        goodsInfo01.setPrice(10.88);
        goodsInfo01.setType(GoodsType.WENYU.getCode());

        GoodsInfo goodsInfo02 = new GoodsInfo();
        goodsInfo02.setCount(10);
        goodsInfo02.setPrice(20.88);
        goodsInfo02.setType(GoodsType.WENYU.getCode());

        info.setGoodsInfos(Arrays.asList(goodsInfo01, goodsInfo02));

        //满减优惠券
        SettlementInfo.CouponAndTemplateInfo manJianInfo = new SettlementInfo.CouponAndTemplateInfo();
        manJianInfo.setId(1);

        CouponTemplateSDK manjianTemplate = new CouponTemplateSDK();
        manjianTemplate.setId(1);
        manjianTemplate.setCategory(CouponCategory.MANJIAN.getCode());
        manjianTemplate.setKey("100120210322");

        TemplateRule manjianRule = new TemplateRule();
        manjianRule.setDiscount(new TemplateRule.Discount(20, 199));
        manjianRule.setUsage(new TemplateRule.Usage("广东省", "广州市",
                JSON.toJSONString(Arrays.asList(GoodsType.WENYU.getCode(), GoodsType.JIAJU.getCode()))));
        manjianRule.setWeight(JSON.toJSONString(Collections.emptyList()));

        manjianTemplate.setRule(manjianRule);
        manJianInfo.setTemplate(manjianTemplate);

        //折扣优惠券
        SettlementInfo.CouponAndTemplateInfo zheKouInfo = new SettlementInfo.CouponAndTemplateInfo();
        zheKouInfo.setId(1);

        CouponTemplateSDK zhekouTemplate = new CouponTemplateSDK();
        zhekouTemplate.setId(2);
        zhekouTemplate.setCategory(CouponCategory.ZHEKOU.getCode());
        zhekouTemplate.setKey("100120210323");

        TemplateRule zhekouRule = new TemplateRule();
        zhekouRule.setDiscount(new TemplateRule.Discount(85, 1));
        zhekouRule.setUsage(new TemplateRule.Usage("广东省", "广州市",
                JSON.toJSONString(Arrays.asList(GoodsType.WENYU.getCode(), GoodsType.JIAJU.getCode()))));
        zhekouRule.setWeight(JSON.toJSONString(
                Collections.singletonList("1001202103220001")
        ));
        zhekouTemplate.setRule(zhekouRule);
        zheKouInfo.setTemplate(zhekouTemplate);

        info.setCouponAndTemplateInfos(Arrays.asList(manJianInfo,zheKouInfo));
        return info;

    }
}

package cn.zeroclian.github.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Desciption fake 商品信息
 * @Author ZeroClian
 * @Date 2021-03-03-16:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfo {
    /**
     * 商品类型
     */
    private Integer type;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 商品数量
     */
    private Integer count;

    // TODO 名称及其他信息

}

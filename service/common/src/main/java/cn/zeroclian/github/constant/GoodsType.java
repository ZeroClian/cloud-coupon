package cn.zeroclian.github.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.tomcat.util.http.fileupload.util.Streams;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Desciption 商品类型枚举
 * @Author ZeroClian
 * @Date 2021-03-03-15:50
 */
@Getter
@AllArgsConstructor
public enum GoodsType {

    WENYU("文娱", 1),
    SHENGXIAN("生鲜", 2),
    JIAJU("家居", 3),
    OTHER("其他", 4),
    ALL("全品类", 5);

    /**
     * 商品类型描述
     */
    private String description;
    /**
     * 商品类型编码
     */
    private Integer code;

    public static GoodsType of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exists!"));
    }
}

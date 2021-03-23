package cn.zeroclian.github.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Desciption  URL 路径信息实体类
 * @Author ZeroClian
 * @Date 2021-03-23-11:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon_path")
public class Path {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;

    /** 路径模式 */
    @Basic
    @Column(name = "path_pattern",nullable = false)
    private String pathPattern;

    /** HTTP 方法类型 */
    @Basic
    @Column(name = "http_method",nullable = false)
    private String httpMethod;

    /** 路径名称 */
    @Basic
    @Column(name = "path_name",nullable = false)
    private String pathName;

    /** 服务名称 */
    @Basic
    @Column(name = "service_name",nullable = false)
    private String serviceName;

    /** 操作模式 : 读 or写 */
    @Basic
    @Column(name = "op_mode",nullable = false)
    private String opMode;

    /**
     * 不带主键的构造函数
     */
    public Path(String pathPattern, String httpMethod, String pathName,
                String serviceName, String opMode) {
        this.pathPattern = pathPattern;
        this.httpMethod = httpMethod;
        this.pathName = pathName;
        this.serviceName = serviceName;
        this.opMode = opMode;
    }
}

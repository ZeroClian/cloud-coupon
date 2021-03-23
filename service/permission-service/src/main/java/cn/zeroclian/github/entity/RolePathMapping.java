package cn.zeroclian.github.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Desciption  Role 与 Path 的映射关系
 * @Author ZeroClian
 * @Date 2021-03-23-12:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon_role_path_mapping")
public class RolePathMapping {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;

    /** Role 表的主键 */
    @Basic
    @Column(name = "role_id",nullable = false)
    private Integer roleId;

    /** Path 表的主键 */
    @Basic
    @Column(name = "path_id",nullable = false)
    private Integer pathId;
}

package cn.zeroclian.github.dao;

import cn.zeroclian.github.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Desciption Role Dao
 * @Author ZeroClian
 * @Date 2021-03-23-13:29
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
}

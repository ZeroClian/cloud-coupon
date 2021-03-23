package cn.zeroclian.github.dao;

import cn.zeroclian.github.entity.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Desciption  UserRoleMapping Dao
 * @Author ZeroClian
 * @Date 2021-03-23-13:35
 */
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Integer> {

    /**
     * 根据 用户id 寻找数据记录
     * @param userId    用户id
     * @return
     */
    UserRoleMapping findByUserId(Long userId);
}

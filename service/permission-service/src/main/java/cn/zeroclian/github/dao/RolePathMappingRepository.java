package cn.zeroclian.github.dao;

import cn.zeroclian.github.entity.RolePathMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Desciption RolePathMapping Dao
 * @Author ZeroClian
 * @Date 2021-03-23-13:31
 */
public interface RolePathMappingRepository
        extends JpaRepository<RolePathMapping, Integer> {

    /**
     * 根据 角色id + 路径id 寻找数据记录
     *
     * @param roleId 角色id
     * @param pathId 路径id
     * @return
     */
    RolePathMapping findByRoleIdAndPathId(Integer roleId, Integer pathId);
}

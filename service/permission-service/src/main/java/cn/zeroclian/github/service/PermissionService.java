package cn.zeroclian.github.service;

import cn.zeroclian.github.constant.RoleEnum;
import cn.zeroclian.github.dao.PathRepository;
import cn.zeroclian.github.dao.RolePathMappingRepository;
import cn.zeroclian.github.dao.RoleRepository;
import cn.zeroclian.github.dao.UserRoleMappingRepository;
import cn.zeroclian.github.entity.Path;
import cn.zeroclian.github.entity.Role;
import cn.zeroclian.github.entity.RolePathMapping;
import cn.zeroclian.github.entity.UserRoleMapping;
import cn.zeroclian.github.exception.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Desciption 权限校验功能服务接口实现
 * @Author ZeroClian
 * @Date 2021-03-23-14:48
 */
@Slf4j
@Service
public class PermissionService {

    private final PathRepository pathRepository;
    private final RoleRepository roleRepository;
    private final RolePathMappingRepository rolePathMappingRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;

    @Autowired
    public PermissionService(PathRepository pathRepository,
                             RoleRepository roleRepository,
                             RolePathMappingRepository rolePathMappingRepository,
                             UserRoleMappingRepository userRoleMappingRepository) {
        this.pathRepository = pathRepository;
        this.roleRepository = roleRepository;
        this.rolePathMappingRepository = rolePathMappingRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
    }

    /**
     * 用户访问接口权限校验
     *
     * @param userId     用户id
     * @param uri        访问uri
     * @param httpMethod 请求类型
     * @return true or false
     */
    public Boolean checkPermission(Long userId, String uri, String httpMethod) {
        UserRoleMapping userRoleMapping = userRoleMappingRepository.findByUserId(userId);
        //如果用户角色映射表找不到记录，直接返回 false
        if (null == userRoleMapping) {
            log.error("userId not exist is UserRoleMapping: {}", userId);
            return false;
        }

        //如果找不到对应的 Role 记录，直接返回 false
        Optional<Role> role = roleRepository.findById(userRoleMapping.getRoleId());
        if (!role.isPresent()) {
            log.error("roleId not exist in Role: {}", userRoleMapping.getRoleId());
            return false;
        }

        //如果用户角色是超级管理员，直接返回 true
        if (role.get().getRoleTag().equals(RoleEnum.SUPER_ADMIN.name())) {
            return true;
        }

        Path path = pathRepository.findByPathPatternAndHttpMethod(uri, httpMethod);
        //如果路径没有注册（忽略），直接返回 true
        if (null == path) {
            return true;
        }

        RolePathMapping rolePathMapping = rolePathMappingRepository
                .findByRoleIdAndPathId(role.get().getId(), path.getId());

        return rolePathMapping != null;

    }
}

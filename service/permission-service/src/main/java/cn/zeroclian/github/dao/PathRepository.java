package cn.zeroclian.github.dao;

import cn.zeroclian.github.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Desciption Path Dao
 * @Author ZeroClian
 * @Date 2021-03-23-13:26
 */
public interface PathRepository extends JpaRepository<Path, Integer> {

    /**
     * 根据服务名称查找 Path 记录
     *
     * @param serviceName 服务名称
     * @return
     */
    List<Path> findAllByServiceName(String serviceName);

    /**
     * 根据 路径模式 + 请求类型 查找数据记录
     *
     * @param pathPattern 路径模式
     * @param httpMethod  请求类型
     * @return
     */
    Path findByPathPatternAndHttpMethod(String pathPattern, String httpMethod);
}

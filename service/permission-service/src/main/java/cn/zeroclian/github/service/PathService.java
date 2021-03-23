package cn.zeroclian.github.service;

import cn.zeroclian.github.dao.PathRepository;
import cn.zeroclian.github.entity.Path;
import cn.zeroclian.github.vo.CreatePathRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Desciption  路径创建功能微服务接口实现
 * @Author ZeroClian
 * @Date 2021-03-23-14:03
 */
@Slf4j
@Service
public class PathService {

    private final PathRepository pathRepository;

    @Autowired
    public PathService(PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    }

    /**
     * 添加新的 Path 到数据表中
     * @param request  {@link CreatePathRequest}
     * @return Path 数据记录的主键
     */
    public List<Integer> createPath(CreatePathRequest request){

        List<CreatePathRequest.PathInfo> pathInfos = request.getPathInfos();
        List<CreatePathRequest.PathInfo> vaildRequests = new ArrayList<>(request.getPathInfos().size());
        List<Path> currentPaths = pathRepository.findAllByServiceName(
                pathInfos.get(0).getServiceName()
        );
        if (!CollectionUtils.isEmpty(currentPaths)){
            for (CreatePathRequest.PathInfo pathInfo : pathInfos) {
                boolean isValid = true;
                for (Path currentPath : currentPaths) {
                    if (currentPath.getPathPattern().equals(pathInfo.getPathPattern())
                    && currentPath.getHttpMethod().equals(pathInfo.getHttpMethod())){
                        isValid = false;
                        break;
                    }
                }
                if (isValid){
                    vaildRequests.add(pathInfo);
                }
            }
        }else {
            vaildRequests = pathInfos;
        }

        List<Path> paths = new ArrayList<>(vaildRequests.size());
        vaildRequests.forEach(p -> paths.add(new Path(
                p.getPathPattern(),
                p.getHttpMethod(),
                p.getPathName(),
                p.getServiceName(),
                p.getOpMode()
        )));

        return pathRepository.saveAll(paths).stream()
                .map(Path::getId).collect(Collectors.toList());
    }
}

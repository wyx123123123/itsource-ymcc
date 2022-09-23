package cn.itsource.mapper;

import cn.itsource.domain.Permission;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据登录人ID查询所拥有的权限
     * @param id
     * @return
     */
    List<Permission> loadPermisisonsByLoginId(Long id);
}

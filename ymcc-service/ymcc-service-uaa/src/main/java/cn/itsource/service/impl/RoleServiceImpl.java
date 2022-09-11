package cn.itsource.service.impl;

import cn.itsource.domain.Role;
import cn.itsource.mapper.RoleMapper;
import cn.itsource.service.IRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}

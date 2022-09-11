package cn.itsource.service.impl;

import cn.itsource.domain.Menu;
import cn.itsource.mapper.MenuMapper;
import cn.itsource.service.IMenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}

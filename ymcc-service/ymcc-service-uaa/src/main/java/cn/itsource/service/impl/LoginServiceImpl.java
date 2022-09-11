package cn.itsource.service.impl;

import cn.itsource.domain.Login;
import cn.itsource.mapper.LoginMapper;
import cn.itsource.service.ILoginService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录表 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements ILoginService {

}

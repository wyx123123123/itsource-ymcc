package cn.itsource.service.impl;

import cn.itsource.domain.LoginLog;
import cn.itsource.mapper.LoginLogMapper;
import cn.itsource.service.ILoginLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录记录 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {

}

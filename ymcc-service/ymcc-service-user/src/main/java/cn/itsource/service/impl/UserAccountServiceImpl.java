package cn.itsource.service.impl;

import cn.itsource.domain.UserAccount;
import cn.itsource.mapper.UserAccountMapper;
import cn.itsource.service.IUserAccountService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements IUserAccountService {

}

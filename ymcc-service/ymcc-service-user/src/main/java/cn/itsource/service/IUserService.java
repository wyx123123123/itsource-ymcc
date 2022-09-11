package cn.itsource.service;

import cn.itsource.domain.User;
import cn.itsource.dto.PhoneRegisterDto;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 会员登录账号 服务类
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
public interface IUserService extends IService<User> {

    // 手机号注册
    void register(PhoneRegisterDto phoneRegisterDto);
}

package cn.itsource.service.impl;

import cn.itsource.domain.UserAddress;
import cn.itsource.mapper.UserAddressMapper;
import cn.itsource.service.IUserAddressService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收货地址 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements IUserAddressService {

}

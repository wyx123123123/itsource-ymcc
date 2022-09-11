package cn.itsource.service.impl;

import cn.itsource.domain.UserBaseInfo;
import cn.itsource.mapper.UserBaseInfoMapper;
import cn.itsource.service.IUserBaseInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员基本信息 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
@Service
public class UserBaseInfoServiceImpl extends ServiceImpl<UserBaseInfoMapper, UserBaseInfo> implements IUserBaseInfoService {

}

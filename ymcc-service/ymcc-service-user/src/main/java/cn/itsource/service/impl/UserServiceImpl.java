package cn.itsource.service.impl;

import cn.itsource.constants.RedisRegistConstant;
import cn.itsource.domain.Login;
import cn.itsource.domain.User;
import cn.itsource.domain.UserAccount;
import cn.itsource.domain.UserBaseInfo;
import cn.itsource.dto.PhoneRegisterDto;
import cn.itsource.dto.RedisRegisterDto;
import cn.itsource.enums.GlobalEnumCode;
import cn.itsource.UaaFeignService;
import cn.itsource.mapper.UserMapper;
import cn.itsource.result.JsonResult;
import cn.itsource.service.IUserAccountService;
import cn.itsource.service.IUserBaseInfoService;
import cn.itsource.service.IUserService;
import cn.itsource.util.AssertUtil;
import cn.itsource.util.BitStatesUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 会员登录账号 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UaaFeignService uaaFeignService;

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private IUserBaseInfoService userBaseInfoService;

    @Override
    //@Transactional
    @GlobalTransactional // 加了这个注解就不要加@Transactional
    public void register(PhoneRegisterDto phoneRegisterDto) {
        String smsCode = phoneRegisterDto.getSmsCode();
        Integer regChannel = phoneRegisterDto.getRegChannel();
        String password = phoneRegisterDto.getPassword();
        String mobile = phoneRegisterDto.getMobile();
        //1.参数校验使用JSR303校验
        //2.拼接业务键，去redis中获取验证码，判断是否跟用户传递的相等
        String redisKey = String.format(RedisRegistConstant.PHONE_REGISTER_PREFIX, mobile);
        Object redisValueTmp = redisTemplate.opsForValue().get(redisKey);
        //2.1.查询不到：抛出异常
        AssertUtil.isNotNull(redisValueTmp, GlobalEnumCode.CODE_EXPIRED_ERROR);
        //2.2.不相等：抛出异常
        RedisRegisterDto redisRegisterDto = (RedisRegisterDto) redisValueTmp;
        AssertUtil.isEquals(smsCode, redisRegisterDto.getCode(), GlobalEnumCode.CODE_INCORRECT_ERROR);
        //3.根据手机号查询user，判断是否以及注册过，如果注册过抛出异常
        User user = selectUserByPhone(mobile);
        AssertUtil.isNull(user, GlobalEnumCode.USER_EXISTED_ERROR);
        //4.调用uaa服务保存login，得到uaa服务返回的主键ID
        Long loginId = saveLogin(password, mobile);
        //5.保存user
        Long userId = saveUser(mobile, loginId);
        //6.保存UserAccount
        saveUserAccount(userId);
        //7.保存UserBaseInfo
        saveUserBaseInfo(regChannel, userId);
    }

    private void saveUserBaseInfo(Integer regChannel, Long userId) {
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setId(userId);
        userBaseInfo.setRegChannel(regChannel);
        userBaseInfo.setCreateTime(new Date().getTime());
        userBaseInfoService.insert(userBaseInfo);
    }

    private void saveUserAccount(Long userId) {
        UserAccount userAccount = new UserAccount();
        // @TODO 应该给用户发送一条短信，告知用户支付初始密码
        userAccount.setPassword("123456");
        userAccount.setId(userId);
        userAccount.setCreateTime(new Date().getTime());
        userAccountService.insert(userAccount);
    }

    private Long saveUser(String mobile, Long loginId) {
        User saveUser = new User();
        saveUser.setNickName(mobile);
        saveUser.setCreateTime(new Date().getTime());
        saveUser.setPhone(mobile);
        // 设置用户手机号注册成功之后的位状态
        long bitState = BitStatesUtils.addState(BitStatesUtils.OP_REGISTED, BitStatesUtils.OP_ACTIVED);
        bitState = BitStatesUtils.addState(bitState, BitStatesUtils.OP_AUTHED_PHONE);
        saveUser.setBitState(bitState);
        saveUser.setLoginId(loginId);
        insert(saveUser);
        return saveUser.getId();
    }

    private Long saveLogin(String password, String mobile) {
        Login login = new Login();
        login.setUsername(mobile);
        login.setPassword(password);
        login.setType(1);
        login.setEnabled(true);
        // 调用uaa服务进行Login保存
        JsonResult jsonResult = uaaFeignService.saveOrUpdate(login);
        // 判断是否保存成功
        AssertUtil.isTrue(jsonResult.isSuccess(), GlobalEnumCode.USER_REGISTER_ERROR);
        Long loginId = Long.valueOf(jsonResult.getData().toString());
        AssertUtil.isNotNull(loginId, GlobalEnumCode.USER_REGISTER_ERROR);
        return loginId;
    }

    /*
     * @Description: 根据电话号码查询用户
     * @Author: Director
     * @Date: 2022/9/1 15:21
     * @param mobile: 电话号码
     * @return: cn.itsource.domain.User
     **/
    private User selectUserByPhone(String mobile) {
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("phone", mobile);
        return selectOne(wrapper);
    }

}

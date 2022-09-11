package cn.itsource.service.impl;

import cn.itsource.constants.RedisRegistConstant;
import cn.itsource.domain.MessageSms;
import cn.itsource.dto.RedisRegisterDto;
import cn.itsource.enums.GlobalEnumCode;
import cn.itsource.exception.GlobalCustomException;
import cn.itsource.properties.RegisterProperties;
import cn.itsource.service.IMessageSmsService;
import cn.itsource.service.IVerifyCodeService;
import cn.itsource.util.AssertUtil;
import cn.itsource.util.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: java0412-ymcc
 * @BelongsPackage: cn.itsource.service.impl
 * @Author: Director
 * @CreateTime: 2022-08-28  11:28
 * @Description: 验证码校验
 * @Version: 1.0
 */
@Service
@Slf4j
public class VerifyCodeServiceImpl implements IVerifyCodeService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private IMessageSmsService messageSmsService;

    // 1.公司要求高度封装玩法   2.mapper没有帮你处理业务，service层方法帮你处理了业务那么此时肯定调用service层
    // 比如：mapper方法直接保存，但是service层方法可以帮你把某个值+10，你现在又需要+10，那就直接调用业务层方法
    @Autowired
    private RegisterProperties registerProperties;

    @Override
    public void sendSmsCode(String mobile) {
        Date date = new Date();
        //1.接收电话号码
        //2.手机号为空、格式校验，我们可以使用AssertUtil，断言工具类进行校验
        AssertUtil.isPhone(mobile, GlobalEnumCode.PHONE_FORMAT_ERROR);
        //3.@TODO 如果做了图形验证码、黑名单功能、是否注册过校验，那么可以在此处进行校验
        //4.拼接业务键+手机号，去redis中查询，这种参数是不可变的，我们可以放在常量类中
        String redisKey = String.format(RedisRegistConstant.PHONE_REGISTER_PREFIX, mobile);
        Object valueTmp = redisTemplate.opsForValue().get(redisKey);
        String code = "";
        if (valueTmp != null){//1.存在
            //1.1.将值中的时间拿出来跟现在的时间做比较，判断是否大于60S
            RedisRegisterDto redisValue = (RedisRegisterDto) valueTmp;
            if (date.getTime() - redisValue.getDateTime() < registerProperties.getInterval()){
                //如果没有大于，那么抛出异常
                throw new GlobalCustomException(GlobalEnumCode.INTERVAL_ERROR);
            }
            //1.2.如果大于60秒，那么我们在此处得到之前的验证码
            code = redisValue.getCode();
        }else {
            //2.不存在,创建一个新的验证码
            code = StrUtils.getRandomString(registerProperties.getCodesize());
        }
        //5.将验证码存入到redis中，设置5分钟有效
        RedisRegisterDto redisRegisterDto = new RedisRegisterDto();
        redisRegisterDto.setCode(code);
        redisRegisterDto.setDateTime(date.getTime());
        redisTemplate.opsForValue().set(redisKey, redisRegisterDto, registerProperties.getValidityperiod(), TimeUnit.MILLISECONDS);
        //6.发送短信给用户
        String content = String.format(registerProperties.getContent(), code);
        log.info(content);
        saveMessageSms(date, content);
    }

    /*
     * @Description: 保存短信日志
     * @Author: Director
     * @Date: 2022/9/1 14:43
     * @param date: 时间
     * @param content: 内容
     * @return: void
     **/
    private void saveMessageSms(Date date, String content) {
        //7.保存messagesms日志
        MessageSms messageSms = new MessageSms();
        messageSms.setSendTime(date);
        messageSms.setTitle(registerProperties.getTitle());
        messageSms.setContent(content);
        // currentRequestAttributes 避免 null 提示
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        // 获取请求体 request
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        messageSms.setIp(request.getRemoteAddr());
        messageSmsService.insert(messageSms);
    }

}

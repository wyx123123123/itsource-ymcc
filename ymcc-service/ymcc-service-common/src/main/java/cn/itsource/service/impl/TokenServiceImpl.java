package cn.itsource.service.impl;

import cn.itsource.service.ITokenService;
import cn.itsource.util.AssertUtil;
import cn.itsource.util.StrUtils;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements ITokenService {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 1.参数校验
     * 2.生成token
     * 3.存入redis，返回token
     *   key: 当前登录人Id + courserId
     *   value: token
     *
     *
     * @param courseId   9   3,9
     * @return
     */
    @Override
    public String createToken(String courseId) {
        //1.参数校验
        AssertUtil.isNotNull(courseId,"课程Id不能为空！！！");
        //2.生成token
        String token = StrUtils.getComplexRandomString(6);
        //3.存储redis
        Long loginId = 3L;
        String key = "token:"+loginId+":"+courseId;
        redisTemplate.opsForValue().set(key,token,10, TimeUnit.MINUTES);
        return token;
    }
}

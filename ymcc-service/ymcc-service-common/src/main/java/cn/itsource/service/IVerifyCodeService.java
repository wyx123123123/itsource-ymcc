package cn.itsource.service;

public interface IVerifyCodeService {
    // 发送短信验证码
    void sendSmsCode(String mobile);
}

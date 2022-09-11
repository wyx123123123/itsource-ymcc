package cn.itsource.dto;

import cn.itsource.util.AssertUtil;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @BelongsProject: java0412-ymcc
 * @BelongsPackage: cn.itsource.dto
 * @Author: Director
 * @CreateTime: 2022-08-28  16:42
 * @Description: 手机注册临时对象
 * @Version: 1.0
 */
@Data
public class PhoneRegisterDto {

    @NotEmpty(message = "电话号码不能为空！")
    @Pattern(regexp = AssertUtil.PHONE_CHECK, message = "手机格式不正确！")
    private String mobile;
    @Size(min = 6, max = 12, message = "密码长度错误")
    private String password;
    @Size(min = 6, max = 6, message = "图形验证码不正确！")
    private String imageCode;
    @Size(min = 6, max = 6, message = "短信验证码不正确！")
    private String smsCode;

    // 注册渠道
    private Integer regChannel;

}

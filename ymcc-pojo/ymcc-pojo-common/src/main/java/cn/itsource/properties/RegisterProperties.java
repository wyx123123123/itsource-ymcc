package cn.itsource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: java0412-ymcc
 * @BelongsPackage: cn.itsource.properties
 * @Author: Director
 * @CreateTime: 2022-08-28  14:56
 * @Description: 用于读取注册相关配置
 * @Version: 1.0
 */
@Data
@Component
@ConfigurationProperties("verifycode.smsregister")
public class RegisterProperties {

    private Long interval; // 60000
    private Integer codesize;// 6
    private Long validityperiod;// 300000
    private String content;// "亲爱的用户，您的短信验证码为：%S, 请在5分钟内使用！"
    private String title;// "注册验证码"

}

package cn.itsource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @BelongsProject: java0412-ymcc
 * @BelongsPackage: cn.itsource.dto
 * @Author: Director
 * @CreateTime: 2022-08-28  14:40
 * @Description: 注册存入redis中的对象
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisRegisterDto {

    private String code;

    private Long dateTime;

}

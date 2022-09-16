package cn.itsource.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderParamDto {
    //多个课程
    @NotEmpty(message = "课程ID不能为空")
    private List<Long> courseIds;
    //支付方式
    @NotNull(message = "支付方式不能为空")
    private Integer payType;
    //防重复token
    @NotEmpty(message = "token不能为空")
    private String token;
    //标识：0普通订单  1秒杀订单
    @NotNull(message = "类型不能为空")
    private Integer type;


}

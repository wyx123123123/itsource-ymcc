package cn.itsource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 包装生成支付单需要的消息体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order2PayOrderParamDto {

    private BigDecimal amount;
    /**
     * 支付方式:0余额直接，1支付宝，2微信,3银联
     */
    private Integer payType;

    /**
     * 订单号
     */
    private String orderNo;
    private Long userId;
    /**
     * 扩展参数，格式： xx=1&oo=2
     */
    private String extParams;
    /**
     * 描述
     */
    private String subject;
}

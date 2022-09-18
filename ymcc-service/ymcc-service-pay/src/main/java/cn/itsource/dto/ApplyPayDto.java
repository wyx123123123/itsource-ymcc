package cn.itsource.dto;

import lombok.Data;

@Data
public class ApplyPayDto {
    private String orderNo;//订单号
    private Integer payType;//支付方式
    private String callUrl;//同步回调地址，商品详情页的地址
}

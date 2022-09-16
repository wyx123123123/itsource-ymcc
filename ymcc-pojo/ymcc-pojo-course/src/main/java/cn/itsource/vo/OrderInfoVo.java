package cn.itsource.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderInfoVo {
    private List<OrderItemInfoVo> courseInfos = new ArrayList<>();
    private BigDecimal totalAmount = new BigDecimal(0);
}

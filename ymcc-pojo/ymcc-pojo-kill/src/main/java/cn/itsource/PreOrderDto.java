package cn.itsource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreOrderDto {

    private String orderNo;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    private Integer totalCount=1;

    private Long userId;

    private Long courseId;
}

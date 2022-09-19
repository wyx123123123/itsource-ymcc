package cn.itsource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayResultDto {
    private String orderNo;
    private String extParams;// loginId + courseIds

    /*private Long loginId;
    private String courseIds;*/
}

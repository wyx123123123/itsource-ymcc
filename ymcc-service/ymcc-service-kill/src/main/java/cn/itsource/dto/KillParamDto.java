package cn.itsource.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class KillParamDto {
    @NotNull(message = "秒杀ID不能为空")
    private Long killCourseId;
    @NotNull(message = "活动ID不能为空")
    private Long activityId;
}

package cn.itsource.dto;

import cn.itsource.domain.*;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @BelongsProject: java0412-ymcc
 * @BelongsPackage: cn.itsource.dto
 * @Author: Director
 * @CreateTime: 2022-09-04  16:08
 * @Description: course新增时的dto
 * @Version: 1.0
 */
@Data
public class CourseDto {

    // 课程
    @Valid
    private Course course;
    // 课程详情
    @Valid
    private CourseDetail courseDetail;
    // 课程营销
    @Valid
    private CourseMarket courseMarket;
    // 课程资源
    @Valid
    private CourseResource courseResource;
    // 当前课程的老师id集合
    @NotEmpty
    private List<Long> teacherIds;


}

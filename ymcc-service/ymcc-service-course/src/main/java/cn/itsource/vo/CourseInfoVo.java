package cn.itsource.vo;

import cn.itsource.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfoVo {
    private Course course;// 课程基本信息
    private CourseDetail courseDetail;//课程详情
    private CourseSummary courseSummary;//课程统计
    private CourseMarket courseMarket;//课程销售相关
    private List<CourseChapter> courseChapters;//课程章节 ----> 一个章节下面还有多个视屏MediaFile
    private List<Teacher> teachers;// 课程老师

}

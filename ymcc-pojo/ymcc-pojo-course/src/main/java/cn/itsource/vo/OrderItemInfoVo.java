package cn.itsource.vo;

import cn.itsource.domain.Course;
import cn.itsource.domain.CourseMarket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemInfoVo {
    private Course course;//课程基本信息
    private CourseMarket courseMarket;//课程销售
}

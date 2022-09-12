package cn.itsource.service;

import cn.itsource.domain.Course;
import cn.itsource.dto.CourseDto;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
public interface ICourseService extends IService<Course> {

    void save(CourseDto courseDto);

    /**
     * 发布课程
     * @param courseId
     */
    void onLineCourse(Long courseId);
}

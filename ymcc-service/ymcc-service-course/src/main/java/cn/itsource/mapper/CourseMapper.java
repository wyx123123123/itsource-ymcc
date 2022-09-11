package cn.itsource.mapper;

import cn.itsource.domain.Course;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
public interface CourseMapper extends BaseMapper<Course> {

    void insertCourseTeacher(@Param("courseId") Long courseId, @Param("teacherIds") List<Long> teacherIds);
}

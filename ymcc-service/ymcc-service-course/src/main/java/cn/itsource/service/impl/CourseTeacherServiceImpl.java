package cn.itsource.service.impl;

import cn.itsource.domain.CourseTeacher;
import cn.itsource.mapper.CourseTeacherMapper;
import cn.itsource.service.ICourseTeacherService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程和老师的中间表 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements ICourseTeacherService {

}

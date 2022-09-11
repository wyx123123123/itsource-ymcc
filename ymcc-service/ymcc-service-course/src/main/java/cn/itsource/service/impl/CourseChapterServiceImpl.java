package cn.itsource.service.impl;

import cn.itsource.domain.CourseChapter;
import cn.itsource.mapper.CourseChapterMapper;
import cn.itsource.service.ICourseChapterService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程章节 ， 一个课程，多个章节，一个章节，多个视频 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
@Service
public class CourseChapterServiceImpl extends ServiceImpl<CourseChapterMapper, CourseChapter> implements ICourseChapterService {

    @Override
    public List<CourseChapter> listByCourseId(Long courseId) {
        Wrapper<CourseChapter> wrapper = new EntityWrapper<>();
        wrapper.eq("course_id", courseId);
        return selectList(wrapper);
    }
}

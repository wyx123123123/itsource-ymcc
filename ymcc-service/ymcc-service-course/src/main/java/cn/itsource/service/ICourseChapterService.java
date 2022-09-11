package cn.itsource.service;

import cn.itsource.domain.CourseChapter;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 课程章节 ， 一个课程，多个章节，一个章节，多个视频 服务类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
public interface ICourseChapterService extends IService<CourseChapter> {

    List<CourseChapter> listByCourseId(Long courseId);
}

package cn.itsource.service;

import cn.itsource.doc.CourseDoc;
import cn.itsource.dto.CourseSearchDto;
import cn.itsource.result.PageList;

import java.util.List;

public interface ICourseEsService {

    void saveCourseDoc(CourseDoc courseDoc);

    /**
     * 课程列表查询
     * @param dto
     * @return
     */
    PageList<CourseDoc> searchCourse(CourseSearchDto dto);
}

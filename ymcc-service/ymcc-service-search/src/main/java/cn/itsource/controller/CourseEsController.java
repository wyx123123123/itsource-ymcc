package cn.itsource.controller;

import cn.itsource.doc.CourseDoc;
import cn.itsource.dto.CourseSearchDto;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import cn.itsource.service.ICourseEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class CourseEsController {
    @Autowired
    private ICourseEsService courseEsService;

    /**
     * 保存课程数据到Es
     * @return
     */
    @PostMapping("/saveCourse")
    public JsonResult saveCourseDoc(@RequestBody CourseDoc courseDoc){
        courseEsService.saveCourseDoc(courseDoc);
        return JsonResult.success();
    }

    /**
     * 课程列表页查询
     * @param dto
     * @return
     */
    @PostMapping("/course")
    public JsonResult searchCourse(@RequestBody CourseSearchDto dto){
        PageList<CourseDoc> courseDocs = courseEsService.searchCourse(dto);
        return JsonResult.success(courseDocs);
    }
}

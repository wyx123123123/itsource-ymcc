package cn.itsource;


import cn.itsource.doc.CourseDoc;
import cn.itsource.result.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-search", fallbackFactory = CourseEsFeignClientFallbackFactory.class)
public interface CourseEsFeignClient {

    @PostMapping("/search/saveCourse")
    JsonResult saveCourseDoc(@RequestBody CourseDoc courseDoc);
}

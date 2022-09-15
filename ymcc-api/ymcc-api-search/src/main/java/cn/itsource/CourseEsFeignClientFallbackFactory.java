package cn.itsource;

import cn.itsource.doc.CourseDoc;
import cn.itsource.result.JsonResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CourseEsFeignClientFallbackFactory implements FallbackFactory<CourseEsFeignClient> {
    @Override
    public CourseEsFeignClient create(Throwable throwable) {
        return new CourseEsFeignClient() {
            @Override
            public JsonResult saveCourseDoc(CourseDoc courseDoc) {
                return JsonResult.error("哦豁！搜索服务GG了！！");
            }
        };
    }
}

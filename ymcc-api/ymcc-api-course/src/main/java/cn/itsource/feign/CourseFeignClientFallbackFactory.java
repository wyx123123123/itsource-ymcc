package cn.itsource.feign;

import cn.itsource.result.JsonResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CourseFeignClientFallbackFactory implements FallbackFactory<CourseFeignClient> {

    @Override
    public CourseFeignClient create(Throwable throwable) {
        return new CourseFeignClient() {
            @Override
            public JsonResult orderInfo(String courseIds) {
                throwable.printStackTrace();
                return JsonResult.error("课程服务不可达！！");
            }
        };
    }
}

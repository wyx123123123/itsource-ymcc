package cn.itsource.feign;

import cn.itsource.result.JsonResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MeidaFileFeignClientFallbackFactory implements FallbackFactory<MeidaFileFeignClient> {
    @Override
    public MeidaFileFeignClient create(Throwable throwable) {
        return new MeidaFileFeignClient() {
            @Override
            public JsonResult queryMediasByCourserId(Long courseId) {
                throwable.printStackTrace();
                return JsonResult.error("媒体服务不可达！！！！");
            }

            @Override
            public JsonResult get(Long id) {
                throwable.printStackTrace();
                return JsonResult.error("媒体服务不可达！！！！");
            }
        };
    }
}

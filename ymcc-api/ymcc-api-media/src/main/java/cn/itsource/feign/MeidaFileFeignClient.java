package cn.itsource.feign;

import cn.itsource.result.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "service-media",fallbackFactory = MeidaFileFeignClientFallbackFactory.class)
public interface MeidaFileFeignClient {

    @RequestMapping(value = "/mediaFile/course/{courseId}",method = RequestMethod.GET)
    JsonResult queryMediasByCourserId(@PathVariable("courseId") Long courseId);

    @RequestMapping(value = "/mediaFile/{id}",method = RequestMethod.GET)
    JsonResult get(@PathVariable("id")Long id);
}

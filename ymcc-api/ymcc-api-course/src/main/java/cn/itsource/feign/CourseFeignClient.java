package cn.itsource.feign;


import cn.itsource.result.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "service-course", fallbackFactory = CourseFeignClientFallbackFactory.class)
public interface CourseFeignClient {

    @RequestMapping(value="/course/info/{courseIds}",method= RequestMethod.GET)
    JsonResult orderInfo(@PathVariable("courseIds") String courseIds);

}

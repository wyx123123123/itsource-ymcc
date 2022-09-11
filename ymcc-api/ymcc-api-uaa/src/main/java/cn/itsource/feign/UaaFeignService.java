package cn.itsource.feign;

import cn.itsource.domain.Login;
import cn.itsource.fallback.UaaFallbackFactory;
import cn.itsource.result.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "service-uaa", fallbackFactory = UaaFallbackFactory.class)
@RequestMapping("/login")
public interface UaaFeignService {

    @RequestMapping(value="/save",method= RequestMethod.POST)
    JsonResult saveOrUpdate(@RequestBody Login login);

}

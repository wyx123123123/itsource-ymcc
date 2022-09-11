package cn.itsource.fallback;

import cn.itsource.domain.Login;
import cn.itsource.enums.GlobalEnumCode;
import cn.itsource.feign.UaaFeignService;
import cn.itsource.result.JsonResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: java0412-ymcc
 * @BelongsPackage: cn.itsource.fallback
 * @Author: Director
 * @CreateTime: 2022-08-28  17:26
 * @Description: Uaa服务的降级类
 * @Version: 1.0
 */
@Component
public class UaaFallbackFactory implements FallbackFactory<UaaFeignService> {
    @Override
    public UaaFeignService create(Throwable throwable) {
        return new UaaFeignService() {
            @Override
            public JsonResult saveOrUpdate(Login login) {
                return JsonResult.error(GlobalEnumCode.ERROR);
            }
        };
    }
}

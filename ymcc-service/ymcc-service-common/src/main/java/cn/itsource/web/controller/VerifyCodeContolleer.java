package cn.itsource.web.controller;

import cn.itsource.result.JsonResult;
import cn.itsource.service.IVerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * @BelongsProject: java0412-ymcc
 * @BelongsPackage: cn.itsource.web.controller
 * @Author: Director
 * @CreateTime: 2022-08-28  11:25
 * @Description: 校验code接口
 * @Version: 1.0
 */
@RequestMapping("/verifycode")
@RestController
public class VerifyCodeContolleer {

    @Autowired
    private IVerifyCodeService verifyCodeService;

    @GetMapping("/sendSmsCode/{mobile}")
    public JsonResult sendSmsCode(@PathVariable("mobile") String mobile){
        verifyCodeService.sendSmsCode(mobile);
        return JsonResult.success();
    }

}

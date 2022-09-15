package cn.itsource.web.controller;

import cn.itsource.result.JsonResult;
import cn.itsource.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    @Autowired
    private ITokenService tokenService;

    /**
     * 生成防重复token
     * @param courseId
     * @return
     */
    @GetMapping("/createToken/{courseId}")
    public JsonResult createToken(@PathVariable("courseId") Long courseId){
       String token =  tokenService.createToken(courseId);
       return JsonResult.success(token);

    }


}

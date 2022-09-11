package cn.itsource.exception;

import cn.itsource.enums.GlobalEnumCode;
import cn.itsource.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    // 此注解标识当前方法要捕捉的异常类型
    @ExceptionHandler(Exception.class)
    public JsonResult exceptionHandler(Exception e){
        e.printStackTrace();
        log.info("发生了exception异常：" + e.getMessage());
        return JsonResult.error(GlobalEnumCode.ERROR);
    }

    // 捕捉我们的自定义异常
    @ExceptionHandler(GlobalCustomException.class)
    public JsonResult globalCustomExceptionHandler(GlobalCustomException e){
        e.printStackTrace();
        log.info("发生了GlobalCustomException异常：" + e.getErrorMessage() + "--" + e.getErrorCode());
        return JsonResult.error(e);
    }

    // 此注解标识当前方法要捕捉的异常类型
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        e.printStackTrace();

        StringBuilder sbr = new StringBuilder();

        e.getBindingResult().getAllErrors().forEach(item->{
            sbr.append(item.getDefaultMessage()).append(",");
        });

        log.info("发生了exception异常：" + sbr.toString());
        return JsonResult.error(sbr.toString(),"10000");
    }

}

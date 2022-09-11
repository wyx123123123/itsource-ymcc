package cn.itsource.exception;

import cn.itsource.enums.GlobalEnumCode;
import lombok.Data;

@Data
public class GlobalCustomException extends RuntimeException{

    private String errorMessage;

    private String errorCode;


    public GlobalCustomException(){
        super();
    }

    public GlobalCustomException(String message){
        super(message);
    }

    // 自定义异常参数的有参构造
    public GlobalCustomException(String errorMessage, String errorCode){
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    // 自定义异常参数的有参构造
    public GlobalCustomException(GlobalEnumCode globalEnumCode){
        super(globalEnumCode.getErrorMessage());
        this.errorMessage = globalEnumCode.getErrorMessage();
        this.errorCode = globalEnumCode.getErrorCode();
    }



}

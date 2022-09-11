package cn.itsource.result;

import cn.itsource.enums.GlobalEnumCode;
import cn.itsource.exception.GlobalCustomException;
import lombok.Data;

//返回JSON结果
@Data
//建造者模式
//@Builder
public class JsonResult {

    private boolean success = true;

    private String message = "成功";

    //错误码，用来描述错误类型 ，1000 表示么有错误
    private String code = "1000";

    //返回的数据
    private Object data;

    /** 创建当前实例 **/
    public static JsonResult success(){
        return new JsonResult();
    }
    /** 创建当前实例 **/
    public static JsonResult success(Object obj){
        JsonResult instance = new JsonResult();
        instance.setData(obj);
        return instance;
    }

    public static JsonResult success(Object obj, String code){
        JsonResult instance = new JsonResult();
        instance.setCode(code);
        instance.setData(obj);
        return instance;
    }
    /** 创建当前实例 **/

    public static JsonResult error(String message, String code){
        JsonResult instance = new JsonResult();
        instance.setMessage(message);
        instance.setSuccess(false);
        instance.setCode(code);
        return instance;
    }

    public static JsonResult error(GlobalEnumCode globalEnumCode){
        JsonResult instance = new JsonResult();
        instance.setMessage(globalEnumCode.getErrorMessage());
        instance.setSuccess(false);
        instance.setCode(globalEnumCode.getErrorCode());
        return instance;
    }

    public static JsonResult error(GlobalCustomException e){
        JsonResult instance = new JsonResult();
        instance.setMessage(e.getErrorMessage());
        instance.setSuccess(false);
        instance.setCode(e.getErrorCode());
        return instance;
    }

    public static JsonResult error(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(false);
        return jsonResult;
    }

    /** 创建当前实例 **/
    public static JsonResult error(String message){
        return error(message,null);
    }

}

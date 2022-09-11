package cn.itsource.util;

import cn.itsource.enums.GlobalEnumCode;
import cn.itsource.exception.GlobalCustomException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssertUtil {

    public static final String PHONE_CHECK = "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$";

    //手机的正则表达式
    private static final Pattern CHINA_PATTERN_PHONE = Pattern.compile(PHONE_CHECK);


    /**--------------------------------------------------------
    手机号断言
    --------------------------------------------------------**/
    public static void isPhone(String phone, GlobalEnumCode globalEnumCode){
        isNotEmpty(phone, GlobalEnumCode.PARAM_IS_NULL_ERROR);
        Matcher m = CHINA_PATTERN_PHONE.matcher(phone);
        if(!m.matches()){
            throw new GlobalCustomException(globalEnumCode);
        }
    }


    /**--------------------------------------------------------
    断言 不为空，如果为空，抛异常
    --------------------------------------------------------**/
    public static void isNotEmpty(String text,GlobalEnumCode globalEnumCode) {
        if (text == null || text.trim().length() == 0) {
            throw new GlobalCustomException(globalEnumCode);
        }
    }


    /**--------------------------------------------------------
    断言对象为空
    --------------------------------------------------------**/
    public static void isNull(Object obj ,GlobalEnumCode globalEnumCode){
        if(obj != null){
            throw new GlobalCustomException(globalEnumCode);
        }
    }
    public static void isNotNull(Object obj ,GlobalEnumCode globalEnumCode){
        if(obj == null){
            throw new GlobalCustomException(globalEnumCode);
        }
    }

    /**--------------------------------------------------------
     断言false,如果为true,我报错
     --------------------------------------------------------**/
    public static void isFalse(boolean isFalse ,GlobalEnumCode globalEnumCode){
        if(isFalse){
            throw new GlobalCustomException(globalEnumCode);
        }
    }
    public static void isTrue(boolean isTrue ,GlobalEnumCode globalEnumCode){
        if(!isTrue){
            throw new GlobalCustomException(globalEnumCode);
        }
    }


    /**--------------------------------------------------------
     断言两个字符串一致
     --------------------------------------------------------**/
    public static void isEquals(String s1,String s2 ,GlobalEnumCode globalEnumCode){
        isNotEmpty(s1, GlobalEnumCode.PARAM_IS_NULL_ERROR);
        isNotEmpty(s2, GlobalEnumCode.PARAM_IS_NULL_ERROR);
        if(!s1.equals(s2)){
            throw new GlobalCustomException(globalEnumCode);
        }
    }
    public static void isEqualsTrim(String s1,String s2 ,GlobalEnumCode globalEnumCode){
        isNotEmpty(s1, GlobalEnumCode.PARAM_IS_NULL_ERROR);
        isNotEmpty(s2, GlobalEnumCode.PARAM_IS_NULL_ERROR);
        if(!s1.trim().equals(s2.trim())){
            throw new GlobalCustomException(globalEnumCode);
        }
    }

    public static void isEqualsIgnoreCase(String s1,String s2 ,GlobalEnumCode globalEnumCode){
        isNotEmpty(s1, GlobalEnumCode.PARAM_IS_NULL_ERROR);
        isNotEmpty(s2, GlobalEnumCode.PARAM_IS_NULL_ERROR);
        if(!s1.trim().equalsIgnoreCase(s2.trim())){
            throw new GlobalCustomException(globalEnumCode);
        }
    }

}

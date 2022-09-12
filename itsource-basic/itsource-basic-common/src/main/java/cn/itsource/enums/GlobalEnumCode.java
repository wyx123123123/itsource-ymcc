package cn.itsource.enums;

public enum GlobalEnumCode {

    // 成功
    OK("成功","0"),
    // 失败
    ERROR("我们正在抢救程序员，请稍后重试！", "-1"),

    // 公共异常以100XX开头
    PARAM_IS_NULL_ERROR("参数不能为空！", "10001"),

    // system服务异常以200XX开头

    // user服务异常以300XX开头
    USER_EXISTED_ERROR("用户已存在！","30001"),
    USER_REGISTER_ERROR("用户注册失败！","30002"),

    // uaa服务异常以400XX开头


    // common服务异常以500XX开头
    PHONE_IS_NULL_ERROR("手机号不能为空！","50001"),
    INTERVAL_ERROR("时间未过一分钟，请勿重复获取验证码！ ","50002"),
    PHONE_FORMAT_ERROR("手机格式不正确！", "50003"),
    CODE_EXPIRED_ERROR("验证码已过期！", "50004"),
    CODE_INCORRECT_ERROR("验证码不正确！", "50005"),


    // course服务异常以600XX开头
    COURSE_EXISTED_ERROR("课程已存在！", "60001"),
    COURSE_NOTEXIST_ERROR("小子想搞事？？ 哥屋恩！！", "60002");





    private String errorMessage;

    private String errorCode;

    GlobalEnumCode(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

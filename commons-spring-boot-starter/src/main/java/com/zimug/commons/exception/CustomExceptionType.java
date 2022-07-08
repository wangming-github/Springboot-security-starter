package com.zimug.commons.exception;

/**
 * @author maizi
 */

public enum CustomExceptionType {


    USER_INPUT_ERROR(400, "您输入的数据格式错误！"),   //
    SC_NOT_FOUND(404, "未找到您请求的资源！"),   //
    SC_FORBIDDEN(403, "您没有权限访问资源！"),   //
    SYSTEM_ERROR(500, "系统出现异常，请您稍后再试或联系管理员！");   //


    private int code; //code
    private String description;//异常类型中文描述


    CustomExceptionType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

}

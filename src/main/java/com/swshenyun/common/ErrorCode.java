package com.swshenyun.common;

/**
 * 自定义错误码
 */
public enum ErrorCode {

    PARAMS_ERROR(40000, "请求参数错误"),
    PASSWORD_ERROR(40001,"密码错误"),
    CHECK_NOT_PASS(40002,"参数校验不通过"),
    PARAMS_NULL_ERROR(40003, "请求参数为空"),
    ACCOUNT_EXISTS(40004, "账户已存在"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    ACCOUNT_LOCKED(40102, "账号被锁定"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    ACCOUNT_NOT_FOUND(40401, "账户不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),

    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    REGISTER_ERROR(50002, "注册插入失败");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 错误信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

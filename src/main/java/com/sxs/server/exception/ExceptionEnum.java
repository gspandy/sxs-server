package com.sxs.server.exception;

/**
 * 异常枚举定义
 */
public enum ExceptionEnum {

    DEFAULT_EXCEPTION(10000, "未知错误"),

    PARAMETER_NULL_EXCEPTION(10001, "参数为空"),

    UNSUPPORTED_OPERATION_EXCEPTION(10002, "不支持的操作"),

    MISSING_PARAMETER_EXCEPTION(10003, "缺少参数");


    private int code;
    private String message;

    private ExceptionEnum(int code, String message) {
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

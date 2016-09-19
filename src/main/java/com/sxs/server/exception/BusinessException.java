package com.sxs.server.exception;


/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BusinessException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }

    public BusinessException(ExceptionEnum exceptionEnum, String additionalMsg) {
        super(exceptionEnum.getMessage() + " [" + additionalMsg + "]");
        this.code = exceptionEnum.getCode();
    }

    public int getCode() {
        return code;
    }
}

package com.sxs.server.exception;

/**
 * 自定义thrift运行异常类
 */
public class ThriftRuntimeException extends Exception {

    private static final long serialVersionUID = -9196549395126558963L;

    public ThriftRuntimeException() {
    }

    public ThriftRuntimeException(String string) {
        super(string);
    }

}

package com.sxs.server.exception;

/**
 * 自定义thrift运行异常类
 */
public class ThriftException extends RuntimeException {

    private static final long serialVersionUID = -9196549395126558963L;

    public ThriftException() {
    }

    public ThriftException(String string) {
        super(string);
    }

    public ThriftException(String msg, Throwable e) {
        super(msg, e);
    }
}

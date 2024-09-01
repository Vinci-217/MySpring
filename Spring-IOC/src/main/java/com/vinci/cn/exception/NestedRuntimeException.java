package com.vinci.cn.exception;

/**
 * 运行时异常的嵌套异常类
 */
public class NestedRuntimeException extends RuntimeException {

    public NestedRuntimeException() {
    }

    public NestedRuntimeException(String message) {
        super(message);
    }

    public NestedRuntimeException(Throwable cause) {
        super(cause);
    }

    public NestedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}

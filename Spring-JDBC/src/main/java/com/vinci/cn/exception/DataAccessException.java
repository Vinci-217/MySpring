package com.vinci.cn.exception;

/**
 * 异常类：数据访问异常
 */
public class DataAccessException extends NestedRuntimeException {

    public DataAccessException() {
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}

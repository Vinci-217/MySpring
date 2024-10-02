package com.vinci.cn.exception;

/**
 * 异常类：事务异常
 */
public class TransactionException extends DataAccessException {

    public TransactionException() {
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

}

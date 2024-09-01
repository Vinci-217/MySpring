package com.vinci.cn.exception;

/**
 * 不成功的依赖注入异常
 */
public class UnsatisfiedDependencyException extends BeanCreationException {

    public UnsatisfiedDependencyException() {
    }

    public UnsatisfiedDependencyException(String message) {
        super(message);
    }

    public UnsatisfiedDependencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsatisfiedDependencyException(Throwable cause) {
        super(cause);
    }

}

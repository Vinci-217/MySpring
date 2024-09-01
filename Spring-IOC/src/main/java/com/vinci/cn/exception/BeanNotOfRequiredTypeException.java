package com.vinci.cn.exception;

/**
 * Bean没有正确的类型异常
 */
public class BeanNotOfRequiredTypeException extends BeansException {

    public BeanNotOfRequiredTypeException() {
    }

    public BeanNotOfRequiredTypeException(String message) {
        super(message);
    }
}

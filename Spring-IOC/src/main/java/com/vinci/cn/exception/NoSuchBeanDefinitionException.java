package com.vinci.cn.exception;

/**
 * 没有这样的bena定义异常
 */
public class NoSuchBeanDefinitionException extends BeanDefinitionException {

    public NoSuchBeanDefinitionException() {
    }

    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }
}

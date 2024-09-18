package com.vinci.cn.context;

/**
 * 回调接口，在Bean实例化、初始化、属性设置之前后，可以进行一些自定义操作。
 */
public interface BeanPostProcessor {

    /**
     * Invoked after new Bean().
     * 这部分是允许开发者自定义的，由于可能造成Bean操作以后不一致，
     * 所以每次调用之后都需要判断Bean是否一致，并且及时更新BeanDefinition。
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * Invoked after bean.init() called.
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * Invoked before bean.setXyz() called.
     */
    default Object postProcessOnSetProperty(Object bean, String beanName) {
        return bean;
    }
}

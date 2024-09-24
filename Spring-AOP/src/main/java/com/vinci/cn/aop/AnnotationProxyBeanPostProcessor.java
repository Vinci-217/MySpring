package com.vinci.cn.aop;



import com.vinci.cn.context.ApplicationContextUtils;
import com.vinci.cn.context.BeanDefinition;
import com.vinci.cn.context.BeanPostProcessor;
import com.vinci.cn.context.ConfigurableApplicationContext;
import com.vinci.cn.exception.AopConfigException;
import com.vinci.cn.exception.BeansException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class AnnotationProxyBeanPostProcessor<A extends Annotation> implements BeanPostProcessor {

    // 原始bean
    Map<String, Object> originBeans = new HashMap<>();
    // 注解类型
    Class<A> annotationClass;

    public AnnotationProxyBeanPostProcessor() {
        this.annotationClass = getParameterizedType();
    }

    /**
     * 在Bean初始化之前对其进行处理
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        // 判断是否有注解
        A anno = beanClass.getAnnotation(annotationClass);
        if (anno != null) {
            // 获取注解的值
            String handlerName;
            try {
                handlerName = (String) anno.annotationType().getMethod("value").invoke(anno);
            } catch (ReflectiveOperationException e) {
                throw new AopConfigException(String.format("@%s must have value() returned String type.", this.annotationClass.getSimpleName()), e);
            }
            // 创建代理
            Object proxy = createProxy(beanClass, bean, handlerName);
            originBeans.put(beanName, bean);
            return proxy;
        } else {
            return bean;
        }
    }

    /**
     * 为指定的Bean创建代理对象
     * @param beanClass
     * @param bean
     * @param handlerName
     * @return
     */
    Object createProxy(Class<?> beanClass, Object bean, String handlerName) {
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) ApplicationContextUtils.getRequiredApplicationContext();

        BeanDefinition def = ctx.findBeanDefinition(handlerName);
        if (def == null) {
            throw new AopConfigException(String.format("@%s proxy handler '%s' not found.", this.annotationClass.getSimpleName(), handlerName));
        }
        Object handlerBean = def.getInstance();
        if (handlerBean == null) {
            handlerBean = ctx.createBeanAsEarlySingleton(def);
        }
        if (handlerBean instanceof InvocationHandler handler) {
            return ProxyResolver.getInstance().createProxy(bean, handler);
        } else {
            throw new AopConfigException(String.format("@%s proxy handler '%s' is not type of %s.", this.annotationClass.getSimpleName(), handlerName,
                    InvocationHandler.class.getName()));
        }
    }

    @Override
    public Object postProcessOnSetProperty(Object bean, String beanName) {
        Object origin = this.originBeans.get(beanName);
        return origin != null ? origin : bean;
    }

    /**
     * 获取当前类的父类的第一个参数化类型
     * @return
     */
    @SuppressWarnings("unchecked")
    private Class<A> getParameterizedType() {
        // 获取当前类的直接父类的泛型类型
        Type type = getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Class " + getClass().getName() + " does not have parameterized type.");
        }
        // 将泛型类型强转为ParameterizedType
        ParameterizedType pt = (ParameterizedType) type;
        // 获取泛型类型实际参数
        Type[] types = pt.getActualTypeArguments();
        // 如果参数个数不等于1，则抛出异常
        if (types.length != 1) {
            throw new IllegalArgumentException("Class " + getClass().getName() + " has more than 1 parameterized types.");
        }
        Type r = types[0];
        // 如果类型不是Class的实例，则抛出异常
        if (!(r instanceof Class<?>)) {
            throw new IllegalArgumentException("Class " + getClass().getName() + " does not have parameterized type of class.");
        }
        return (Class<A>) r;
    }
}

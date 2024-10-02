package com.vinci.cn.jdbc.tx;

import com.vinci.cn.annotation.Transactional;
import com.vinci.cn.aop.AnnotationProxyBeanPostProcessor;

/**
 * 注解 {@link Transactional} 的 BeanPostProcessor
 */
public class TransactionalBeanPostProcessor extends AnnotationProxyBeanPostProcessor<Transactional> {

}

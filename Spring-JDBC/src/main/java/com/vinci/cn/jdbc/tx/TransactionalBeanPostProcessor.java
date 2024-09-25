package com.vinci.cn.jdbc.tx;

import com.vinci.cn.annotation.Transactional;
import com.vinci.cn.aop.AnnotationProxyBeanPostProcessor;

public class TransactionalBeanPostProcessor extends AnnotationProxyBeanPostProcessor<Transactional> {

}

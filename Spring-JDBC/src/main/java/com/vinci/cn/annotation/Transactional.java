package com.vinci.cn.annotation;

import java.lang.annotation.*;

/**
 * 注解类，用于标注事务注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Transactional {

    String value() default "platformTransactionManager";
}

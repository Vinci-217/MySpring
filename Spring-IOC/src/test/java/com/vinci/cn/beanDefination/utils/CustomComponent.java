package com.vinci.cn.beanDefination.utils;



import com.vinci.cn.annotation.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CustomComponent {

    String value() default "";

}

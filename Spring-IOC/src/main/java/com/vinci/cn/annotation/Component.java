package com.vinci.cn.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    /**
     * Bean的名称，默认为空
     * @return
     */
    String value() default "";

}

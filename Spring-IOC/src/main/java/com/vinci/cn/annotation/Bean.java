package com.vinci.cn.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    /**
     * Bean的名称，默认为空
     * @return
     */
    String value() default "";

    /**
     * Bean的初始化方法名称，默认为空
     * @return
     */
    String initMethod() default "";

    /**
     * Bean的销毁方法名称，默认为空
     * @return
     */
    String destroyMethod() default "";
}

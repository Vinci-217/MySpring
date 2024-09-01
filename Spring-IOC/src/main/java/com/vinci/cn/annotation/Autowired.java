package com.vinci.cn.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Autowired {

    /**
     * 是否需要自动装配
     * @return
     */
    boolean value() default true;

    /**
     * Bean的名称
     * @return
     */
    String name() default "";
}

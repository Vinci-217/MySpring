package com.vinci.cn.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {

    /**
     * 需要扫描的包名
     * @return
     */
    String[] value() default {};

}

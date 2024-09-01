package com.vinci.cn.annotation;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Order {

    /**
     * 排序值
     * @return
     */
    int value();

}

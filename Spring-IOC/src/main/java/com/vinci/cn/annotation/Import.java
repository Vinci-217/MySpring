package com.vinci.cn.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

    /**
     * 需要导入的类
     * @return
     */
    Class<?>[] value();

}

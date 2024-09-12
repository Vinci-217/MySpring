package com.vinci.cn.beanInit.scan.nested;

import com.vinci.cn.annotation.Component;

@Component
public class OuterBean {

    @Component
    public static class NestedBean {

    }
}

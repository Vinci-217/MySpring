package com.vinci.cn.ioc.scan.nested;

import com.vinci.cn.annotation.Component;

@Component
public class OuterBean {

    @Component
    public static class NestedBean {

    }
}

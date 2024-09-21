package com.vinci.cn.aop.after;

import com.vinci.cn.annotation.Around;
import com.vinci.cn.annotation.Component;

@Component
@Around("politeInvocationHandler")
public class GreetingBean {

    public String hello(String name) {
        return "Hello, " + name + ".";
    }

    public String morning(String name) {
        return "Morning, " + name + ".";
    }
}

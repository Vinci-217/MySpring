package com.vinci.cn.aop.around;

import com.vinci.cn.annotation.Around;
import com.vinci.cn.annotation.Component;
import com.vinci.cn.annotation.Value;

@Component
@Around("aroundInvocationHandler")
public class OriginBean {

    @Value("${customer.name}")
    public String name;

    @Polite
    public String hello() {
        return "Hello, " + name + ".";
    }

    public String morning() {
        return "Morning, " + name + ".";
    }
}

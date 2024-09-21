package com.vinci.cn.aop.before;

import com.vinci.cn.annotation.Around;
import com.vinci.cn.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Around("logInvocationHandler")
public class BusinessBean {

    final Logger logger = LoggerFactory.getLogger(getClass());

    public String hello(String name) {
        logger.info("Hello, {}.", name);
        return "Hello, " + name + ".";
    }

    public String morning(String name) {
        logger.info("Morning, {}.", name);
        return "Morning, " + name + ".";
    }
}

package com.vinci.cn.aop.after;

import com.vinci.cn.context.AnnotationConfigApplicationContext;
import com.vinci.cn.property.PropertyResolver;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AfterProxyTest {

    @Test
    public void testAfterProxy() {
        try (var ctx = new AnnotationConfigApplicationContext(AfterApplication.class, createPropertyResolver())) {
            GreetingBean proxy = ctx.getBean(GreetingBean.class);
            // should change return value:
            assertEquals("Hello, Bob!", proxy.hello("Bob"));
            assertEquals("Morning, Alice!", proxy.morning("Alice"));
        }
    }

    PropertyResolver createPropertyResolver() {
        var ps = new Properties();
        var pr = new PropertyResolver(ps);
        return pr;
    }
}

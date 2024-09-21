package com.vinci.cn.aop.before;

import com.vinci.cn.context.AnnotationConfigApplicationContext;
import com.vinci.cn.property.PropertyResolver;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeforeProxyTest {

    @Test
    public void testBeforeProxy() {
        try (var ctx = new AnnotationConfigApplicationContext(BeforeApplication.class, createPropertyResolver())) {
            BusinessBean proxy = ctx.getBean(BusinessBean.class);
            // should print log:
            assertEquals("Hello, Bob.", proxy.hello("Bob"));
            assertEquals("Morning, Alice.", proxy.morning("Alice"));
        }
    }

    PropertyResolver createPropertyResolver() {
        var ps = new Properties();
        var pr = new PropertyResolver(ps);
        return pr;
    }
}

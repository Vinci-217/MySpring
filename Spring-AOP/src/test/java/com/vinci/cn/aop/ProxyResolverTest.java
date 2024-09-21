package com.vinci.cn.aop;

import com.vinci.cn.ProxyResolver;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ProxyResolverTest {

    @Test
    public void testProxyResolver() {
        OriginBean origin = new OriginBean();
        origin.name = "Bob";

        assertEquals("Hello, Bob.", origin.hello());

        // 创建代理：
        OriginBean proxy = new ProxyResolver().createProxy(origin, new PoliteInvocationHandler());

        // 输出代理类名，类似OriginBean$CGLIB$xxxx:
        System.out.println(proxy.getClass().getName());

        // 确保代理类不是原始类:
        assertNotSame(OriginBean.class, proxy.getClass());
        // 代理对象的name应该为null:
        assertNull(proxy.name);

        // 带@Polite:
        assertEquals("Hello, Bob!", proxy.hello());
        // 不带@Polite:
        assertEquals("Morning, Bob.", proxy.morning());
    }
}

package com.vinci.cn.proxyResolver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理执行类，获取被代理的对象和方法，执行代理任务
 */
public class PoliteInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object bean, Method method, Object[] args) throws Throwable {
        // 修改标记了@Polite的方法返回值:
        if (method.getAnnotation(Polite.class) != null) {
            String ret = (String) method.invoke(bean, args);
            if (ret.endsWith(".")) {
                ret = ret.substring(0, ret.length() - 1) + "!";
            }
            return ret;
        }
        return method.invoke(bean, args);
    }

    public PoliteInvocationHandler() {
    }
}

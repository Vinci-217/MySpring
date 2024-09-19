package com.vinci.cn;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create proxy by subclassing and override methods with interceptor using CGLIB.
 */
public class ProxyResolver {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("unchecked")
    public <T> T createProxy(T bean, MethodInterceptor interceptor) {
        Class<?> targetClass = bean.getClass();
        logger.atDebug().log("create proxy for bean {} @{}", targetClass.getName(), Integer.toHexString(bean.hashCode()));

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, java.lang.reflect.Method method, Object[] args, MethodProxy proxy) throws Throwable {
                // 调用自定义的拦截器
                return interceptor.intercept(obj, method, args, proxy);
            }
        });

        // 创建代理对象
        T proxy = (T) enhancer.create();
        return proxy;
    }
}

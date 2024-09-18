package com.vinci.cn.ioc.scan.proxy;

import com.vinci.cn.annotation.Autowired;
import com.vinci.cn.annotation.Component;

@Component
public class InjectProxyOnConstructorBean {

    public final OriginBean injected;

    public InjectProxyOnConstructorBean(@Autowired OriginBean injected) {
        this.injected = injected;
    }
}

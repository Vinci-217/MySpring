package com.vinci.cn.beanPostProcessor.scan.proxy;

import com.vinci.cn.annotation.Autowired;
import com.vinci.cn.annotation.Component;

@Component
public class InjectProxyOnPropertyBean {

    @Autowired
    public OriginBean injected;
}

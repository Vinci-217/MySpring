package com.vinci.cn.aop.around;

import com.vinci.cn.annotation.Autowired;
import com.vinci.cn.annotation.Component;
import com.vinci.cn.annotation.Order;

@Order(0)
@Component
public class OtherBean {

    public OriginBean origin;

    public OtherBean(@Autowired OriginBean origin) {
        this.origin = origin;
    }
}

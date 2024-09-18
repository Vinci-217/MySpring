package com.vinci.cn.ioc.scan.proxy;

import com.vinci.cn.annotation.Component;
import com.vinci.cn.annotation.Value;

@Component
public class OriginBean {

    @Value("${app.title}")
    public String name;

    public String version;

    @Value("${app.version}")
    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return this.version;
    }

}

package com.vinci.cn.beanCreation.scan.init;

import com.vinci.cn.annotation.Component;
import com.vinci.cn.annotation.Value;
import jakarta.annotation.PostConstruct;

@Component
public class AnnotationInitBean {

    @Value("${app.title}")
    String appTitle;

    @Value("${app.version}")
    String appVersion;

    public String appName;

    @PostConstruct
    void init() {
        this.appName = this.appTitle + " / " + this.appVersion;
    }
}

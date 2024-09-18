package com.vinci.cn.ioc.scan.destroy;

import com.vinci.cn.annotation.Component;
import com.vinci.cn.annotation.Value;
import jakarta.annotation.PreDestroy;

@Component
public class AnnotationDestroyBean {

    @Value("${app.title}")
    public String appTitle;

    @PreDestroy
    void destroy() {
        this.appTitle = null;
    }
}

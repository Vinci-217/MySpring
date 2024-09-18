package com.vinci.cn.ioc.scan.destroy;

import com.vinci.cn.annotation.Bean;
import com.vinci.cn.annotation.Configuration;
import com.vinci.cn.annotation.Value;

@Configuration
public class SpecifyDestroyConfiguration {

    @Bean(destroyMethod = "destroy")
    SpecifyDestroyBean createSpecifyDestroyBean(@Value("${app.title}") String appTitle) {
        return new SpecifyDestroyBean(appTitle);
    }
}

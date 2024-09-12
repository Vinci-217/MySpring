package com.vinci.cn.beanCreation.scan.init;

import com.vinci.cn.annotation.Bean;
import com.vinci.cn.annotation.Configuration;
import com.vinci.cn.annotation.Value;

@Configuration
public class SpecifyInitConfiguration {

    @Bean(initMethod = "init")
    SpecifyInitBean createSpecifyInitBean(@Value("${app.title}") String appTitle, @Value("${app.version}") String appVersion) {
        return new SpecifyInitBean(appTitle, appVersion);
    }
}

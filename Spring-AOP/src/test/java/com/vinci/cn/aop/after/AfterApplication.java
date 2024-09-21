package com.vinci.cn.aop.after;

import com.vinci.cn.annotation.Bean;
import com.vinci.cn.annotation.ComponentScan;
import com.vinci.cn.annotation.Configuration;
import com.vinci.cn.aop.AroundProxyBeanPostProcessor;

@Configuration
@ComponentScan
public class AfterApplication {

    @Bean
    AroundProxyBeanPostProcessor createAroundProxyBeanPostProcessor() {
        return new AroundProxyBeanPostProcessor();
    }
}

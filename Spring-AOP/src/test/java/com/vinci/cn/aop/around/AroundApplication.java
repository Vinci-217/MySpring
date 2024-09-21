package com.vinci.cn.aop.around;

import com.vinci.cn.annotation.Bean;
import com.vinci.cn.annotation.ComponentScan;
import com.vinci.cn.annotation.Configuration;
import com.vinci.cn.aop.AroundProxyBeanPostProcessor;

@Configuration
@ComponentScan
public class AroundApplication {

    @Bean
    AroundProxyBeanPostProcessor createAroundProxyBeanPostProcessor() {
        return new AroundProxyBeanPostProcessor();
    }
}

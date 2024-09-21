package com.vinci.cn.aop.before;

import com.vinci.cn.annotation.Bean;
import com.vinci.cn.annotation.ComponentScan;
import com.vinci.cn.annotation.Configuration;
import com.vinci.cn.aop.AroundProxyBeanPostProcessor;

@Configuration
@ComponentScan
public class BeforeApplication {

    @Bean
    AroundProxyBeanPostProcessor createAroundProxyBeanPostProcessor() {
        return new AroundProxyBeanPostProcessor();
    }
}

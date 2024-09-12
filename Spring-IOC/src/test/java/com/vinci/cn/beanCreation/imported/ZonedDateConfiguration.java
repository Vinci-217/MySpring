package com.vinci.cn.beanCreation.imported;

import com.vinci.cn.annotation.Bean;
import com.vinci.cn.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class ZonedDateConfiguration {

    @Bean
    ZonedDateTime startZonedDateTime() {
        return ZonedDateTime.now();
    }
}

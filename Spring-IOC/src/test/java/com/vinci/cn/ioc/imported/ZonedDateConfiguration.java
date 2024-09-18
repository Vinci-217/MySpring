package com.vinci.cn.ioc.imported;

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

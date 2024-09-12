package com.vinci.cn.beanInit.imported;

import com.vinci.cn.annotation.Bean;
import com.vinci.cn.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class LocalDateConfiguration {

    @Bean
    LocalDate startLocalDate() {
        return LocalDate.now();
    }

    @Bean
    LocalDateTime startLocalDateTime() {
        return LocalDateTime.now();
    }
}

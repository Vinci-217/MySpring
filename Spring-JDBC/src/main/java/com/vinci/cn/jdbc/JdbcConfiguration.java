package com.vinci.cn.jdbc;

import com.vinci.cn.annotation.Autowired;
import com.vinci.cn.annotation.Bean;
import com.vinci.cn.annotation.Configuration;
import com.vinci.cn.annotation.Value;
import com.vinci.cn.jdbc.tx.DataSourceTransactionManager;
import com.vinci.cn.jdbc.tx.PlatformTransactionManager;
import com.vinci.cn.jdbc.tx.TransactionalBeanPostProcessor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * 配置类，用于创建数据源、JdbcTemplate、PlatformTransactionManager等bean
 */
@Configuration
public class JdbcConfiguration {

    /**
     * 读取配置文件中的数据源配置，并创建HikariCP数据源
     * @param url
     * @param username
     * @param password
     * @param driver
     * @param maximumPoolSize
     * @param minimumPoolSize
     * @param connTimeout
     * @return
     */
    @Bean(destroyMethod = "close")
    DataSource dataSource(
            // properties:
            @Value("${summer.datasource.url}") String url, //
            @Value("${summer.datasource.username}") String username, //
            @Value("${summer.datasource.password}") String password, //
            @Value("${summer.datasource.driver-class-name:}") String driver, //
            @Value("${summer.datasource.maximum-pool-size:20}") int maximumPoolSize, //
            @Value("${summer.datasource.minimum-pool-size:1}") int minimumPoolSize, //
            @Value("${summer.datasource.connection-timeout:30000}") int connTimeout //
    ) {
        HikariConfig config = new HikariConfig();
        config.setAutoCommit(false);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        if (driver != null) {
            config.setDriverClassName(driver);
        }
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumPoolSize);
        config.setConnectionTimeout(connTimeout);
        // 一个小细节，这里使用了防御式编程
        return new HikariDataSource(config);
    }

    @Bean
    JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    TransactionalBeanPostProcessor transactionalBeanPostProcessor() {
        return new TransactionalBeanPostProcessor();
    }

    @Bean
    PlatformTransactionManager platformTransactionManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

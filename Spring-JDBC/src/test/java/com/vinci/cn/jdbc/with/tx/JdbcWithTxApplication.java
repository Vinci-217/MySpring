package com.vinci.cn.jdbc.with.tx;

import com.vinci.cn.annotation.ComponentScan;
import com.vinci.cn.annotation.Configuration;
import com.vinci.cn.annotation.Import;
import com.vinci.cn.jdbc.JdbcConfiguration;

@ComponentScan
@Configuration
@Import(JdbcConfiguration.class)
public class JdbcWithTxApplication {

}

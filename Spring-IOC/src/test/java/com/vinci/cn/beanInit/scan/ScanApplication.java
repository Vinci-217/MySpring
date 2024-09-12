package com.vinci.cn.beanInit.scan;

import com.vinci.cn.annotation.ComponentScan;
import com.vinci.cn.annotation.Import;
import com.vinci.cn.beanInit.imported.LocalDateConfiguration;
import com.vinci.cn.beanInit.imported.ZonedDateConfiguration;

/**
 * 应用启动类
 */
@ComponentScan
@Import({ LocalDateConfiguration.class, ZonedDateConfiguration.class })
public class ScanApplication {

}

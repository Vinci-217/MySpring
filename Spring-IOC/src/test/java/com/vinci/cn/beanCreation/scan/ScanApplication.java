package com.vinci.cn.beanCreation.scan;

import com.vinci.cn.beanCreation.imported.LocalDateConfiguration;
import com.vinci.cn.beanCreation.imported.ZonedDateConfiguration;
import com.vinci.cn.annotation.ComponentScan;
import com.vinci.cn.annotation.Import;

@ComponentScan
@Import({ LocalDateConfiguration.class, ZonedDateConfiguration.class })
public class ScanApplication {

}

package com.vinci.cn.beanDefination.scan;


import com.vinci.cn.annotation.ComponentScan;
import com.vinci.cn.annotation.Import;
import com.vinci.cn.beanDefination.imported.LocalDateConfiguration;
import com.vinci.cn.beanDefination.imported.ZonedDateConfiguration;

@ComponentScan
@Import({ LocalDateConfiguration.class, ZonedDateConfiguration.class })
public class ScanApplication {

}

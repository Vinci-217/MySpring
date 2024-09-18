package com.vinci.cn.ioc.scan;

import com.vinci.cn.ioc.imported.LocalDateConfiguration;
import com.vinci.cn.ioc.imported.ZonedDateConfiguration;
import com.vinci.cn.annotation.ComponentScan;
import com.vinci.cn.annotation.Import;

@ComponentScan
@Import({ LocalDateConfiguration.class, ZonedDateConfiguration.class })
public class ScanApplication {

}

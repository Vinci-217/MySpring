package com.vinci.cn.bean.scan;

import com.vinci.cn.bean.imported.LocalDateConfiguration;
import com.vinci.cn.bean.imported.ZonedDateConfiguration;
import com.vinci.cn.annotation.ComponentScan;
import com.vinci.cn.annotation.Import;

@ComponentScan
@Import({ LocalDateConfiguration.class, ZonedDateConfiguration.class })
public class ScanApplication {

}

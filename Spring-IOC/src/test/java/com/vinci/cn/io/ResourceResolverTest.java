package com.vinci.cn.io;

import com.vinci.cn.annoscan.AnnoScan;
import com.vinci.cn.resource.ResourceResolver;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.sql.DataSourceDefinition;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceResolverTest {

    /**
     * 测试扫描指定包下的所有类
     */
    @Test
    public void scanClass() {
        var pkg = "com.vinci.cn.scan";
        var rr = new ResourceResolver(pkg);
        List<String> classes = rr.scan(res -> {
            String name = res.name();
            if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6).replace("/", ".").replace("\\", ".");
            }
            return null;
        });
        Collections.sort(classes);
        System.out.println(classes);
        String[] listClasses = new String[]{
                // list of some scan classes:
                "com.vinci.cn.scan.convert.ValueConverterBean", //
                "com.vinci.cn.scan.destroy.AnnotationDestroyBean", //
                "com.vinci.cn.scan.init.SpecifyInitConfiguration", //
                "com.vinci.cn.scan.proxy.OriginBean", //
                "com.vinci.cn.scan.proxy.FirstProxyBeanPostProcessor", //
                "com.vinci.cn.scan.proxy.SecondProxyBeanPostProcessor", //
                "com.vinci.cn.scan.nested.OuterBean", //
                "com.vinci.cn.scan.nested.OuterBean$NestedBean", //
                "com.vinci.cn.scan.sub1.Sub1Bean", //
                "com.vinci.cn.scan.sub1.sub2.Sub2Bean", //
                "com.vinci.cn.scan.sub1.sub2.sub3.Sub3Bean", //
        };
        for (String clazz : listClasses) {
            assertTrue(classes.contains(clazz));
        }
    }

    /**
     * 测试扫描指定包下的所有jar包
     */
    @Test
    public void scanJar() {
        var pkg = PostConstruct.class.getPackageName();
        var rr = new ResourceResolver(pkg);
        List<String> classes = rr.scan(res -> {
            String name = res.name();
            if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6).replace("/", ".").replace("\\", ".");
            }
            return null;
        });

        var annopkg = "com.vinci.cn.annoscan";
        var annorr = new ResourceResolver(annopkg);
        List<String> annoClasses = annorr.scan(res -> {
            String name = res.name();
            if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6).replace("/", ".").replace("\\", ".");
            }
            return null;
        });
        // jar里面的类也会被扫描到
        assertTrue(classes.contains(PostConstruct.class.getName()));
        assertTrue(classes.contains(PreDestroy.class.getName()));
        assertTrue(classes.contains(PermitAll.class.getName()));
        assertTrue(classes.contains(DataSourceDefinition.class.getName()));
        // 自定义的注解也会被扫描到
        assertTrue(annoClasses.contains(AnnoScan.class.getName()));
    }

    /**
     * 测试扫描指定包下的所有txt文件
     */
    @Test
    public void scanTxt() {
        var pkg = "com.vinci.cn.scan";
        var rr = new ResourceResolver(pkg);
        List<String> classes = rr.scan(res -> {
            String name = res.name();
            if (name.endsWith(".txt")) {
                return name.replace("\\", "/");
            }
            return null;
        });
        Collections.sort(classes);
        assertArrayEquals(new String[]{
                // txt files:
                "com/vinci/cn/scan/sub1/sub1.txt", //
                "com/vinci/cn/scan/sub1/sub2/sub2.txt", //
                "com/vinci/cn/scan/sub1/sub2/sub3/sub3.txt", //
        }, classes.toArray(String[]::new));
    }
}

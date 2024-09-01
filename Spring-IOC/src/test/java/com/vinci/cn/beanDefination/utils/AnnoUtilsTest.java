package com.vinci.cn.beanDefination.utils;

import com.vinci.cn.annotation.Component;
import com.vinci.cn.annotation.Configuration;
import com.vinci.cn.annotation.Order;
import com.vinci.cn.exception.BeanDefinitionException;
import com.vinci.cn.utils.ClassUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnnoUtilsTest {

    /**
     * 测试没有Component注解的类
     * @throws Exception
     */
    @Test
    public void noComponent() throws Exception {
        assertNull(ClassUtils.findAnnotation(Simple.class, Component.class));
    }

    /**
     * 测试有Component注解的类
     * @throws Exception
     */
    @Test
    public void simpleComponent() throws Exception {
        assertNotNull(ClassUtils.findAnnotation(SimpleComponent.class, Component.class));
        assertEquals("simpleComponent", ClassUtils.getBeanName(SimpleComponent.class));
    }

    /**
     * 测试有Component注解的类，指定Bean名称
     * @throws Exception
     */
    @Test
    public void simpleComponentWithName() throws Exception {
        assertNotNull(ClassUtils.findAnnotation(SimpleComponentWithName.class, Component.class));
        assertEquals("simpleName", ClassUtils.getBeanName(SimpleComponentWithName.class));
    }

    /**
     * 测试有Configuration注解的类
     * @throws Exception
     */
    @Test
    public void simpleConfiguration() throws Exception {
        assertNotNull(ClassUtils.findAnnotation(SimpleConfiguration.class, Component.class));
        assertEquals("simpleConfiguration", ClassUtils.getBeanName(SimpleConfiguration.class));
    }

    /**
     * 测试有Configuration注解的类，指定Bean名称
     * @throws Exception
     */
    @Test
    public void simpleConfigurationWithName() throws Exception {
        assertNotNull(ClassUtils.findAnnotation(SimpleConfigurationWithName.class, Component.class));
        assertEquals("simpleCfg", ClassUtils.getBeanName(SimpleConfigurationWithName.class));
    }

    /**
     * 测试自定义注解
     * @throws Exception
     */
    @Test
    public void customComponent() throws Exception {
        assertNotNull(ClassUtils.findAnnotation(Custom.class, Component.class));
        assertEquals("custom", ClassUtils.getBeanName(Custom.class));
    }

    /**
     * 测试自定义注解，指定Bean名称
     * @throws Exception
     */
    @Test
    public void customComponentWithName() throws Exception {
        assertNotNull(ClassUtils.findAnnotation(CustomWithName.class, Component.class));
        assertEquals("customName", ClassUtils.getBeanName(CustomWithName.class));
    }

    /**
     * 测试重复注解
     * @throws Exception
     */
    @Test
    public void duplicateComponent() throws Exception {
        assertThrows(BeanDefinitionException.class, () -> {
            ClassUtils.findAnnotation(DuplicateComponent.class, Component.class);
        });
        assertThrows(BeanDefinitionException.class, () -> {
            ClassUtils.findAnnotation(DuplicateComponent2.class, Component.class);
        });
    }
}

@Order(1)
class Simple {
}

@Component
class SimpleComponent {
}

@Component("simpleName")
class SimpleComponentWithName {
}

@Configuration
class SimpleConfiguration {

}

@Configuration("simpleCfg")
class SimpleConfigurationWithName {

}

@CustomComponent
class Custom {

}

@CustomComponent("customName")
class CustomWithName {

}

@Component
@Configuration
class DuplicateComponent {

}

@CustomComponent
@Configuration
class DuplicateComponent2 {

}

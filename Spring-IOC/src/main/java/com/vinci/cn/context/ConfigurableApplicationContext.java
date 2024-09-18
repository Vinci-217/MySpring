package com.vinci.cn.context;

import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 用户使用的ApplicationContext接口，继承ApplicationContext接口，增加了一些方法，用于获取BeanDefinition、创建早期单例Bean等。
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

    List<BeanDefinition> findBeanDefinitions(Class<?> type);

    @Nullable
    BeanDefinition findBeanDefinition(Class<?> type);

    @Nullable
    BeanDefinition findBeanDefinition(String name);

    @Nullable
    BeanDefinition findBeanDefinition(String name, Class<?> requiredType);

    Object createBeanAsEarlySingleton(BeanDefinition def);
}

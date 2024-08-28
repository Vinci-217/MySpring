package com.vinci.cn.property;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 属性配置解决器，注入配置和属性
 */
public class PropertyResolver {

    Logger logger = LoggerFactory.getLogger(getClass());

    // 存放系统环境变量和系统属性
    Map<String, String> properties = new HashMap<>();

    // 存放类型转换器，作用是将不同的类型进行转换成需要的类型。Map的key是需要转换的类型的字节码，value是转换器的函数。
    Map<Class<?>, Function<String, Object>> converters = new HashMap<>();

    public PropertyResolver(Properties props) {
        // 存入系统环境变量
        this.properties.putAll(System.getenv());
        // 存入系统属性
        Set<String> names = props.stringPropertyNames();
        for (String name : names) {
            this.properties.put(name, props.getProperty(name));
        }
        if (logger.isDebugEnabled()) {
            List<String> keys = new ArrayList<>(this.properties.keySet());
            Collections.sort(keys);
            for (String key : keys) {
                logger.debug("PropertyResolver: {} = {}", key, this.properties.get(key));
            }
        }
        // 注册转换器，相当于自定义一套对于字节码和类型转换函数的规则
        converters.put(String.class, s -> s);
        converters.put(boolean.class, s -> Boolean.parseBoolean(s));
        converters.put(Boolean.class, s -> Boolean.valueOf(s));

        converters.put(byte.class, s -> Byte.parseByte(s));
        converters.put(Byte.class, s -> Byte.valueOf(s));

        converters.put(short.class, s -> Short.parseShort(s));
        converters.put(Short.class, s -> Short.valueOf(s));

        converters.put(int.class, s -> Integer.parseInt(s));
        converters.put(Integer.class, s -> Integer.valueOf(s));

        converters.put(long.class, s -> Long.parseLong(s));
        converters.put(Long.class, s -> Long.valueOf(s));

        converters.put(float.class, s -> Float.parseFloat(s));
        converters.put(Float.class, s -> Float.valueOf(s));

        converters.put(double.class, s -> Double.parseDouble(s));
        converters.put(Double.class, s -> Double.valueOf(s));

        converters.put(LocalDate.class, s -> LocalDate.parse(s));
        converters.put(LocalTime.class, s -> LocalTime.parse(s));
        converters.put(LocalDateTime.class, s -> LocalDateTime.parse(s));
        converters.put(ZonedDateTime.class, s -> ZonedDateTime.parse(s));
        converters.put(Duration.class, s -> Duration.parse(s));
        converters.put(ZoneId.class, s -> ZoneId.of(s));
    }

    /**
     * 检查是否包含某个属性
     *
     * @param key
     * @return
     */
    public boolean containsProperty(String key) {
        return this.properties.containsKey(key);
    }

    /**
     * 获取属性
     *
     * @param key
     * @return
     */
    @Nullable
    public String getProperty(String key) {
        PropertyExpr keyExpr = parsePropertyExpr(key);
        if (keyExpr != null) {
            if (keyExpr.defaultValue() != null) { // 看有没有默认值
                return getProperty(keyExpr.key(), keyExpr.defaultValue());
            } else {
                return getRequiredProperty(keyExpr.key());
            }
        }
        String value = this.properties.get(key);
        if (value != null) {
            return parseValue(value);
        }
        return value;
    }

    /**
     * 获取指定键的属性，如果键不存在就返回默认值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value == null ? parseValue(defaultValue) : value;
    }

    /**
     * 获取指定键的属性，并转换为目标类型
     *
     * @param key
     * @param targetType
     * @param <T>
     * @return
     */
    @Nullable
    public <T> T getProperty(String key, Class<T> targetType) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        return convert(targetType, value);
    }

    /**
     * 获取指定键的实现，并且转换为目标类型
     *
     * @param key
     * @param targetType
     * @param defaultValue
     * @param <T>
     * @return
     */
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return convert(targetType, value);
    }

    /**
     * 获取指定键的属性，如果键不存在就抛出异常
     *
     * @param key
     * @return
     */
    public String getRequiredProperty(String key) {
        String value = getProperty(key);
        return Objects.requireNonNull(value, "Property '" + key + "' not found.");
    }

    /**
     * 获取指定键的属性，并转换为目标类型，如果键不存在就抛出异常
     *
     * @param key
     * @param targetType
     * @param <T>
     * @return
     */
    public <T> T getRequiredProperty(String key, Class<T> targetType) {
        T value = getProperty(key, targetType);
        return Objects.requireNonNull(value, "Property '" + key + "' not found.");
    }

    /**
     * 转换类型：通过字节码和类型转换函数，将字符串转换为对应的类型
     *
     * @param clazz
     * @param value
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    <T> T convert(Class<?> clazz, String value) {
        Function<String, Object> fn = this.converters.get(clazz);
        if (fn == null) {
            throw new IllegalArgumentException("Unsupported value type: " + clazz.getName());
        }
        return (T) fn.apply(value);
    }

    String parseValue(String value) {
        PropertyExpr expr = parsePropertyExpr(value);
        if (expr == null) {
            return value;
        }
        if (expr.defaultValue() != null) {
            return getProperty(expr.key(), expr.defaultValue());
        } else {
            return getRequiredProperty(expr.key());
        }
    }

    /**
     * 解析属性表达式，将字符串转换为属性表达式的形式（下面的记录类）
     *
     * @param key
     * @return
     */
    PropertyExpr parsePropertyExpr(String key) {
        if (key.startsWith("${") && key.endsWith("}")) {
            int n = key.indexOf(':');
            if (n == (-1)) {
                // 没有默认值: ${key}
                String k = notEmpty(key.substring(2, key.length() - 1));
                return new PropertyExpr(k, null);
            } else {
                // 有默认值: ${key:default}
                String k = notEmpty(key.substring(2, n));
                return new PropertyExpr(k, key.substring(n + 1, key.length() - 1));
            }
        }
        return null;
    }

    /**
     * 确保传入的东西不为空
     *
     * @param key
     * @return
     */
    String notEmpty(String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key: " + key);
        }
        return key;
    }
}

/**
 * 记录类，用来表示属性表达式
 *
 * @param key
 * @param defaultValue
 */
record PropertyExpr(String key, String defaultValue) {
}

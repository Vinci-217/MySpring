package com.vinci.cn.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

/**
 * 读取yaml配置文件工具类
 */

@SuppressWarnings("unused")
public class YamlUtils {

    /**
     * 将yaml配置文件转换为Map对象
     *
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadYaml(String path) {
        // loaderOptions 是一个 LoaderOptions 对象，用于配置 YAML 加载器的选项
        var loaderOptions = new LoaderOptions();
        // dumperOptions 是一个 DumperOptions 对象，用于配置 YAML 转储器的选项
        var dumperOptions = new DumperOptions();
        // representer 是一个 Representer 对象，负责将 Java 对象转换为 YAML 格式
        var representer = new Representer(dumperOptions);
        // resolver 是一个 NoImplicitResolver 对象，用于控制 YAML 解析过程中的隐式解析行为
        var resolver = new NoImplicitResolver();
        // yaml 是一个 Yaml 对象，用于加载和解析 YAML 文件
        var yaml = new Yaml(new Constructor(loaderOptions), representer, dumperOptions, loaderOptions, resolver);
        return ClassPathUtils.readInputStream(path, (input) -> {
            return (Map<String, Object>) yaml.load(input);
        });
    }

    public static Map<String, Object> loadYamlAsPlainMap(String path) {
        Map<String, Object> data = loadYaml(path);
        Map<String, Object> plain = new LinkedHashMap<>();
        convertTo(data, "", plain);
        return plain;
    }

    static void convertTo(Map<String, Object> source, String prefix, Map<String, Object> plain) {
        for (String key : source.keySet()) {
            Object value = source.get(key);
            // 解析value的结构
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> subMap = (Map<String, Object>) value;
                convertTo(subMap, prefix + key + ".", plain);
            } else if (value instanceof List) {
                plain.put(prefix + key, value);
            } else {
                plain.put(prefix + key, value.toString());
            }
        }
    }
}


/**
 * 自定义的Resolver，禁止使用隐式解析器
 */
class NoImplicitResolver extends Resolver {

    public NoImplicitResolver() {
        super();
        super.yamlImplicitResolvers.clear(); // 禁止使用隐式解析器
    }
}

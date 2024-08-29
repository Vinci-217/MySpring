package com.vinci.cn.property;


import java.io.IOException;
import java.io.InputStream;

/**
 * 函数式接口
 * @param <T>
 */
@FunctionalInterface
public interface InputStreamCallback<T> {

    T doWithInputStream(InputStream stream) throws IOException;
}

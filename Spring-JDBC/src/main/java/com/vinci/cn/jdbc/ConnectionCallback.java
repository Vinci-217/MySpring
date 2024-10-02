package com.vinci.cn.jdbc;

import jakarta.annotation.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 接口类，用于回调Connection对象
 * @param <T>
 */
@FunctionalInterface
public interface ConnectionCallback<T> {

    // 回调Connection对象，返回结果
    @Nullable
    T doInConnection(Connection con) throws SQLException;

}

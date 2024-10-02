package com.vinci.cn.jdbc;

import jakarta.annotation.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 接口类型，用于对PreparedStatement进行操作，并返回结果
 * @param <T>
 */
@FunctionalInterface
public interface PreparedStatementCallback<T> {

    // 对PreparedStatement进行操作，并返回结果
    @Nullable
    T doInPreparedStatement(PreparedStatement ps) throws SQLException;

}

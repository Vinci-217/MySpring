package com.vinci.cn.jdbc;

import jakarta.annotation.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 接口类，用于从ResultSet中提取数据
 * @param <T>
 */
@FunctionalInterface
public interface ResultSetExtractor<T> {

    @Nullable
    T extractData(ResultSet rs) throws SQLException;

}

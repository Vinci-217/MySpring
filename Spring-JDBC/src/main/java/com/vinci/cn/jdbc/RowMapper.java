package com.vinci.cn.jdbc;

import jakarta.annotation.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 接口类，用于将查询结果集中的每一行映射为一个对象。
 * @param <T>
 */
@FunctionalInterface
public interface RowMapper<T> {

    // 将查询结果集中的每一行映射为一个对象
    @Nullable
    T mapRow(ResultSet rs, int rowNum) throws SQLException;

}

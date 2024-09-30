package com.vinci.cn.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 接口类，用于创建PreparedStatement对象
 */
@FunctionalInterface
public interface PreparedStatementCreator {

    PreparedStatement createPreparedStatement(Connection con) throws SQLException;

}
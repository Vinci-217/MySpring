package com.vinci.cn.jdbc.tx;

import java.sql.Connection;

/**
 * TransactionStatus 事务状态类
 * 里面包含一个 Connection 对象，表示当前事务的连接
 */
public class TransactionStatus {

    final Connection connection;

    public TransactionStatus(Connection connection) {
        this.connection = connection;
    }
}

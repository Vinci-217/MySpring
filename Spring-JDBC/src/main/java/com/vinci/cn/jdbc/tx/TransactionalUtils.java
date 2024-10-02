package com.vinci.cn.jdbc.tx;

import jakarta.annotation.Nullable;

import java.sql.Connection;

/**
 * TransactionalUtils 工具类
 * 用于获取当前事务的连接
 */
public class TransactionalUtils {

    @Nullable
    public static Connection getCurrentConnection() {
        TransactionStatus ts = DataSourceTransactionManager.transactionStatus.get();
        return ts == null ? null : ts.connection;
    }
}

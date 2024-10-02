package com.vinci.cn.jdbc;

import com.vinci.cn.exception.DataAccessException;
import com.vinci.cn.jdbc.tx.TransactionalUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义JdbcTemplate类，封装了常用的JDBC操作，包括查询、更新、事务等。
 */
public class JdbcTemplate {

    final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Number queryForNumber(String sql, Object... args) throws DataAccessException {
        return queryForObject(sql, NumberRowMapper.instance, args);
    }

    /**
     * 查询单个对象，根据返回值类型自动选择RowMapper。
     * @param sql
     * @param clazz
     * @param args
     * @return
     * @param <T>
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public <T> T queryForObject(String sql, Class<T> clazz, Object... args) throws DataAccessException {
        // 根据返回值类型自动选择RowMapper
        if (clazz == String.class) {
            return (T) queryForObject(sql, StringRowMapper.instance, args);
        }
        if (clazz == Boolean.class || clazz == boolean.class) {
            return (T) queryForObject(sql, BooleanRowMapper.instance, args);
        }
        if (Number.class.isAssignableFrom(clazz) || clazz.isPrimitive()) {
            return (T) queryForObject(sql, NumberRowMapper.instance, args);
        }
        return queryForObject(sql, new BeanRowMapper<>(clazz), args);
    }

    /**
     * 查询单个对象，根据RowMapper对象进行映射。
     * @param sql
     * @param rowMapper
     * @param args
     * @return
     * @param <T>
     * @throws DataAccessException
     */
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        return execute(preparedStatementCreator(sql, args),
                // PreparedStatementCallback
                (PreparedStatement ps) -> {
                    T t = null;
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            if (t == null) {
                                t = rowMapper.mapRow(rs, rs.getRow());
                            } else {
                                throw new DataAccessException("Multiple rows found.");
                            }
                        }
                    }
                    if (t == null) {
                        throw new DataAccessException("Empty result set.");
                    }
                    return t;
                });
    }

    /**
     * 查询多个对象，根据返回值类型自动选择RowMapper。
     * @param sql
     * @param clazz
     * @param args
     * @return
     * @param <T>
     * @throws DataAccessException
     */
    public <T> List<T> queryForList(String sql, Class<T> clazz, Object... args) throws DataAccessException {
        return queryForList(sql, new BeanRowMapper<>(clazz), args);
    }

    /**
     * 查询多个对象，根据RowMapper对象进行映射。
     * @param sql
     * @param rowMapper
     * @param args
     * @return
     * @param <T>
     * @throws DataAccessException
     */
    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        // 创建PreparedStatementCreator回调接口的实现类，用于创建PreparedStatement对象。
        return execute(preparedStatementCreator(sql, args),
                // PreparedStatementCallback
                (PreparedStatement ps) -> {
                    List<T> list = new ArrayList<>();
                    // 执行查询，将结果集中的每一行数据映射为对象并添加到List中。
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            list.add(rowMapper.mapRow(rs, rs.getRow()));
                        }
                    }
                    return list;
                });
    }

    /**
     * 更新并返回生成的主键值。
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     */
    public Number updateAndReturnGeneratedKey(String sql, Object... args) throws DataAccessException {
        return execute(
                // PreparedStatementCreator
                (Connection con) -> {
                    var ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    bindArgs(ps, args);
                    return ps;
                },
                // PreparedStatementCallback
                (PreparedStatement ps) -> {
                    int n = ps.executeUpdate();
                    if (n == 0) {
                        throw new DataAccessException("0 rows inserted.");
                    }
                    if (n > 1) {
                        throw new DataAccessException("Multiple rows inserted.");
                    }
                    // 获取生成的主键值
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        while (keys.next()) {
                            return (Number) keys.getObject(1);
                        }
                    }
                    throw new DataAccessException("Should not reach here.");
                });
    }

    /**
     * 更新数据库，并返回更新的行数。
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     */
    public int update(String sql, Object... args) throws DataAccessException {
        return execute(preparedStatementCreator(sql, args),
                // PreparedStatementCallback
                (PreparedStatement ps) -> {
                    return ps.executeUpdate();
                });
    }


    /**
     *
     * @param psc PreparedStatementCreator 回调接口
     * @param action PreparedStatementCallback 回调接口
     * @return
     * @param <T>
     */
    public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) {
        // 调用execute(ConnectionCallback)方法，传入ConnectionCallback接口的实现类
        // 该实现类中调用PreparedStatementCreator的createPreparedStatement方法创建PreparedStatement对象，
        // 并调用PreparedStatementCallback的doInPreparedStatement方法执行PreparedStatement操作，
        // 最后返回操作结果。
        return execute((Connection con) -> {
            try (PreparedStatement ps = psc.createPreparedStatement(con)) {
                // 执行具体的操作并返回结果
                return action.doInPreparedStatement(ps);
            }
        });
    }


    // 这里实际上是实现了回调函数具体的执行
    /**
     * 执行JDBC操作，根据当前事务状态获取连接，如果存在事务则使用事务连接，否则获取新的连接。
     * @param action
     * @return
     * @param <T>
     * @throws DataAccessException
     */
    public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
        // 尝试获取当前事务连接:
        Connection current = TransactionalUtils.getCurrentConnection();
        // 如果存在事务连接，则直接使用该连接执行操作:
        if (current != null) {
            try {
                return action.doInConnection(current);
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }
        // 获取新连接:
        try (Connection newConn = dataSource.getConnection()) {
            final boolean autoCommit = newConn.getAutoCommit();
            if (!autoCommit) {
                newConn.setAutoCommit(true);
            }
            // 执行操作，并返回执行的结果:
            T result = action.doInConnection(newConn);
            if (!autoCommit) {
                newConn.setAutoCommit(false);
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * 创建PreparedStatementCreator回调接口的实现类，用于创建PreparedStatement对象。
     * @param sql
     * @param args
     * @return
     */
    private PreparedStatementCreator preparedStatementCreator(String sql, Object... args) {
        return (Connection con) -> {
            var ps = con.prepareStatement(sql);
            bindArgs(ps, args);
            return ps;
        };
    }

    /**
     * 绑定参数到PreparedStatement对象中，相当于把参数值设置到PreparedStatement对象的占位符上。
     * @param ps
     * @param args
     * @throws SQLException
     */
    private void bindArgs(PreparedStatement ps, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i + 1, args[i]);
        }
    }
}

/**
 * 实现了RowMapper接口，用于将ResultSet中的每一行字符串类型的数据映射为一个对象。
 */
class StringRowMapper implements RowMapper<String> {

    static StringRowMapper instance = new StringRowMapper();

    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString(1);
    }
}

/**
 * 实现了RowMapper接口，用于将ResultSet中的每一行Boolean类型的数据映射为一个对象。
 */
class BooleanRowMapper implements RowMapper<Boolean> {

    static BooleanRowMapper instance = new BooleanRowMapper();

    @Override
    public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getBoolean(1);
    }
}

/**
 * 实现了RowMapper接口，用于将ResultSet中的每一行数据类型的数据映射为一个对象。
 */
class NumberRowMapper implements RowMapper<Number> {

    static NumberRowMapper instance = new NumberRowMapper();

    @Override
    public Number mapRow(ResultSet rs, int rowNum) throws SQLException {
        return (Number) rs.getObject(1);
    }
}

package org.simpleframework.mvc.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.simpleframework.util.CollectionUtil;
import org.simpleframework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作助手类
 * Created by Why on 2017/3/9.
 */
public final class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final QueryRunner QUERY_RUNNER;

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final BasicDataSource DATA_SOURCE;

    static {
        LOGGER.debug("initializing datasource...");
        CONNECTION_HOLDER = new ThreadLocal<Connection>();

        QUERY_RUNNER = new QueryRunner();

        String driver = ConfigHelper.getJdbcDriver();
        String url = ConfigHelper.getJdbcUrl();
        String user = ConfigHelper.getJdbcUser();
        String password = ConfigHelper.getJdbcPassword();

        LOGGER.debug("jdbc driver: " + driver);
        LOGGER.debug("jdbc url: " + url);
        LOGGER.debug("jdbc user: " + user);
        LOGGER.debug("jdbc password: " + password);

        if (StringUtil.isEmpty(driver)) {
            LOGGER.error("jdbc driver must not be empty");
        }

        if (StringUtil.isEmpty(url)) {
            LOGGER.error("jdbc url must not be empty");
        }

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(user);
        DATA_SOURCE.setPassword(password);
        DATA_SOURCE.setInitialSize(1);
        DATA_SOURCE.setMaxWaitMillis(10000);
        DATA_SOURCE.setMinIdle(10);
        DATA_SOURCE.setMaxIdle(20);
        DATA_SOURCE.setMaxTotal(100);
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//        } catch (SQLException e) {
//            LOGGER.error("get connection failure", e);
//        }
//        return conn;
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.debug("get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.debug("close connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
                conn = null;
            }
        }
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.debug("close connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
                conn = null;
            }
        }
    }

    /**
     * 查询实体列表
     */
    public static <T extends Serializable> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = null;
        try {
            LOGGER.debug("sql: " + sql);
            LOGGER.debug("params: " + Arrays.toString(params));
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (Exception e) {
            LOGGER.error("sql: " + sql);
            LOGGER.error("params: " + Arrays.toString(params));
            LOGGER.error("query entiry list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 查询实体列表
     */
    public static <T extends Serializable> List<T> queryEntityList(Class<? extends Serializable> entityClass, ResultSetHandler<List<T>> handler, String sql, Object... params) {
        List<T> entityList = null;
        try {
            LOGGER.debug("sql: " + sql);
            LOGGER.debug("params: " + Arrays.toString(params));
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, handler, params);
        } catch (Exception e) {
            LOGGER.error("sql: " + sql);
            LOGGER.error("params: " + Arrays.toString(params));
            LOGGER.error("query entiry list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 记录数查询
     */
    public static long queryCount(String sql, Object... params) {
        long result;
        try {
            LOGGER.debug("sql: " + sql);
            LOGGER.debug("params: " + Arrays.toString(params));
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new ScalarHandler<Long>(), params);
        } catch (SQLException e) {
            LOGGER.error("sql: " + sql);
            LOGGER.error("params: " + Arrays.toString(params));
            LOGGER.error("query count failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * 查询实体
     */
    public static <T extends Serializable> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity = null;
        try {
            LOGGER.debug("sql: " + sql);
            LOGGER.debug("params: " + Arrays.toString(params));
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (Exception e) {
            LOGGER.error("sql: " + sql);
            LOGGER.error("params: " + Arrays.toString(params));
            LOGGER.error("query entiry failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 查询实体
     */
    public static <T extends Serializable> T queryEntity(Class<T> entityClass, ResultSetHandler<T> handler, String sql, Object... params) {
        T entity = null;
        try {
            LOGGER.debug("sql: " + sql);
            LOGGER.debug("params: " + Arrays.toString(params));
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, handler, params);
        } catch (Exception e) {
            LOGGER.error("sql: " + sql);
            LOGGER.error("params: " + Arrays.toString(params));
            LOGGER.error("query entiry failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 查询 Map List
     */
    public static List<Map<String, Object>> queryMapList(String sql, Object... params) {
        List<Map<String, Object>> mapList = null;
        try {
            LOGGER.debug("sql: " + sql);
            LOGGER.debug("params: " + Arrays.toString(params));
            Connection conn = getConnection();
            mapList = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (Exception e) {
            LOGGER.error("sql: " + sql);
            LOGGER.error("params: " + Arrays.toString(params));
            LOGGER.error("query entiry failure", e);
            throw new RuntimeException(e);
        }
        return mapList;
    }

    /**
     * 执行查询语句
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        try {
            Connection conn = getConnection();
            LOGGER.debug("sql: " + sql);
            LOGGER.debug("params: " + Arrays.toString(params));
            return QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("sql: " + sql);
            LOGGER.error("params: " + Arrays.toString(params));
            LOGGER.error("execute query failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行 SQL 语句（包括update、insert、delete）
     */
    public static int executeUpdate(String sql, Object... params) {
        try {
            Connection conn = getConnection();
            LOGGER.debug("sql: " + sql);
            LOGGER.debug("params: " + Arrays.toString(params));
            return QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("sql: " + sql);
            LOGGER.error("params: " + Arrays.toString(params));
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行批量 SQL 语句（包括update、insert、delete）
     */
    public static int[] executeBatch(String sql, Object[][] params) {
        try {
            Connection conn = getConnection();
            LOGGER.debug("sql: " + sql);
            LOGGER.debug("params: " + Arrays.toString(params));
            return QUERY_RUNNER.batch(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("sql: " + sql);
            LOGGER.error("params: " + Arrays.toString(params));
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行SQL文件
     */
    public static void executeSqlFile(String filePath) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while ((sql = reader.readLine()) != null) {
                DatabaseHelper.executeUpdate(sql);
            }
        } catch (IOException e) {
            LOGGER.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 开启事务
     */
    public static void beginTransaction() {
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.debug("begin tracsaction failure", e);
            throw new RuntimeException(e);
        } finally {
            CONNECTION_HOLDER.set(conn);
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection conn = getConnection();
        try {
            conn.commit();
        } catch (SQLException e) {
            LOGGER.debug("commit tracsaction failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection conn = getConnection();
        try {
            conn.rollback();
        } catch (SQLException e) {
            LOGGER.debug("rollback tracsaction failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * 返回数据源对象
     */
    public static BasicDataSource getDataSource() {
        return DATA_SOURCE;
    }
}

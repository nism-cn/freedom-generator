package org.nism.fg.base.utils;

import cn.hutool.core.io.IoUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.CoreConstant;
import org.nism.fg.domain.entity.DbInfo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据源工具类
 *
 * @author inism
 * @since 1.0.0
 */
@Slf4j
public class DataSourceUtils {

    private DataSourceUtils() {
    }

    /**
     * 初始化druid数据源
     */
    public static DruidDataSource init(String url, String username, String password) {
        Properties properties = new Properties();
        properties.setProperty("remarks", "true");
        properties.setProperty("useInformationSchema", "true");
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setTestWhileIdle(false);
        dataSource.setConnectProperties(properties);
        reloadClassLoader(dataSource);
        return dataSource;
    }

    public static DruidDataSource init(DbInfo db) {
        return init(db.getJdbcUrl(), db.getUsername(), db.getPassword());
    }

    public static void test(DbInfo db) {
        DruidDataSource dataSource = new DruidDataSource();
        Connection connection = null;
        try {
            dataSource.setUrl(db.getJdbcUrl());
            dataSource.setUsername(db.getUsername());
            dataSource.setPassword(db.getPassword());
            dataSource.setMaxWait(500);
            reloadClassLoader(dataSource);
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalArgumentException("获取连接失败, 请检查连接配置.", e);
        } finally {
            IoUtil.close(connection);
            IoUtil.close(dataSource);
        }
    }

    public static void test(DruidDataSource ds) {
        DruidDataSource dataSource = ds.cloneDruidDataSource();
        Connection connection = null;
        try {
            dataSource.setMaxWait(500);
            reloadClassLoader(dataSource);
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalArgumentException("获取连接失败, 请检查连接配置.", e);
        } finally {
            IoUtil.close(connection);
            IoUtil.close(dataSource);
        }
    }

    public static DruidDataSource getDb(String id) {
        DruidDataSource ds = SpringUtils.getBean(CoreConstant.DB_BEAN_KEY + id, DruidDataSource.class);
        reloadClassLoader(ds);
        DataSourceUtils.test(ds);
        return ds;
    }

    private static void reloadClassLoader(DruidDataSource ds) {
        try {
            String driverClassName = JdbcUtils.getDriverClassName(ds.getUrl());
            ClassLoader classLoader = CoreConstant.JDBC_CLASS_LOADER_MAP.get(driverClassName);
            if (classLoader != null && ds.getDriverClassLoader() == null) {
                ds.setDriverClassLoader(classLoader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("通过外部加载jar失败", e);
        }
    }

}

package org.nism.fg.base.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.ds.GlobalDSFactory;
import cn.hutool.db.ds.hikari.HikariDSFactory;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.setting.Setting;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.constant.CoreConstant;
import org.nism.fg.domain.entity.FgDatabaseInfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源工具类
 *
 * @author inism
 * @since 1.0.0
 */
@Slf4j
public class DataSourceUtils {

    public static final String DB_KEY = "IOC_DB";

    static {
        DSFactory.setCurrentDSFactory(new HikariDSFactory(new Setting()));
    }

    private DataSourceUtils() {
    }

    /**
     * 初始化druid数据源
     */
    public static DataSource init(String id, String url, String username, String password) {
        HikariDSFactory factory = (HikariDSFactory) GlobalDSFactory.get();
        factory.close(id);
        Setting setting = factory.getSetting();

        setting.setByGroup("url", id, url);
        setting.setByGroup("username", id, username);
        setting.setByGroup("password", id, password);

        DSFactory dsFactory = GlobalDSFactory.set(new HikariDSFactory(setting));
        return dsFactory.getDataSource(id);
    }

    public static DataSource init(FgDatabaseInfo db) {
        String dsId = CoreConstant.DB_BEAN_KEY + db.getId();
        return init(dsId, db.getJdbcUrl(), db.getUsername(), db.getPassword());
    }

    public static void test(FgDatabaseInfo db) {
        SimpleDataSource dataSource = new SimpleDataSource(db.getJdbcUrl(), db.getUsername(), db.getPassword());
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalArgumentException("获取连接失败, 请检查连接配置.", e);
        } finally {
            IoUtil.close(connection);
            IoUtil.close(dataSource);
        }
    }

    public static void test(DataSource ds) {
        Connection connection = null;
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            throw new IllegalArgumentException("获取连接失败, 请检查连接配置.", e);
        } finally {
            IoUtil.close(connection);
        }
    }

    public static DataSource getDb(String id) {
        HikariDSFactory factory = (HikariDSFactory) GlobalDSFactory.get();

        Setting setting = factory.getSetting();
        System.out.println(setting);
        String s = CoreConstant.DB_BEAN_KEY + id;
//        reloadClassLoader(ds);
//        DataSourceUtils.test(ds);
        DataSource ds = factory.getDataSource(s);
        System.out.println(ds.getClass());
        return ds;
    }

    private static void reloadClassLoader(DataSource ds) {
//        try {
//            String driverClassName = JdbcUtils.getDriverClassName(ds.getUrl());
//            ClassLoader classLoader = CoreConstant.JDBC_CLASS_LOADER_MAP.get(driverClassName);
//            if (classLoader != null && ds.getDriverClassLoader() == null) {
//                ds.setDriverClassLoader(classLoader);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            log.error("通过外部加载jar失败", e);
//        }
    }

}

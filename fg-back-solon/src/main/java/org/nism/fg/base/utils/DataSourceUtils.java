package org.nism.fg.base.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.ds.GlobalDSFactory;
import cn.hutool.db.ds.hikari.HikariDSFactory;
import cn.hutool.setting.Setting;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.constant.CoreConstant;
import org.nism.fg.domain.entity.FgDatabaseInfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * 数据源工具类
 *
 * @author inism
 * @since 1.0.0
 */
@Slf4j
public class DataSourceUtils {

    private static final String TEST = "^TEST^";

    static {
        DSFactory.setCurrentDSFactory(new HikariDSFactory(new Setting()));
    }

    private DataSourceUtils() {
    }

    /**
     * 初始化数据源
     */
    public static DataSource init(String id, String url, String username, String password) {
        HikariDSFactory factory = (HikariDSFactory) GlobalDSFactory.get();
        factory.close(id);
        Setting setting = factory.getSetting();
        Setting ns = new Setting();

        ns.setByGroup("url", id, url);
        ns.setByGroup("username", id, username);
        ns.setByGroup("password", id, password);

        for (String group : setting.getGroups()) {
            if (group.contains(TEST)) {
                continue;
            }
            setting.getMap(group).forEach((k, v) -> ns.putByGroup(k, group, v));
        }

        DSFactory dsFactory = GlobalDSFactory.set(new HikariDSFactory(ns));
        return dsFactory.getDataSource(id);
    }

    public static DataSource init(FgDatabaseInfo db) {
        String dsId = CoreConstant.DB_BEAN_KEY + db.getId();
        return init(dsId, db.getJdbcUrl(), db.getUsername(), db.getPassword());
    }

    public static void test(FgDatabaseInfo db) {
        String id = TEST + UUID.randomUUID().toString().replaceAll("-", "");
        DataSource dataSource = init(id, db.getJdbcUrl(), db.getUsername(), db.getPassword());
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalArgumentException("获取连接失败, 请检查连接配置.", e);
        } finally {
            IoUtil.close(connection);
        }
    }

    public static DataSource getDb(String id) {
        HikariDSFactory factory = (HikariDSFactory) GlobalDSFactory.get();
        System.out.println(factory.getSetting());
        String s = CoreConstant.DB_BEAN_KEY + id;
        return factory.getDataSource(s);
    }


}

package org.nism.fg.base.config;

import com.zaxxer.hikari.HikariDataSource;
import org.nism.fg.base.utils.SystemUtils;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据库配置
 *
 * @author inism
 * @since 1.0.0
 */
@Configuration
public class DataSourceConfig {

    @Bean(value = "db1", typed = true)
    public DataSource dataSource() {
        String db = SystemUtils.getDataDir() + SystemUtils.SEP + "fgdb";
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:file:" + db + ";MODE=MYSQL");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

}

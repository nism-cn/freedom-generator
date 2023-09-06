package org.nism.fg.base.config;

import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.base.utils.SystemUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 数据库配置
 *
 * @author inism
 * @since 1.0.0
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String db = SystemUtils.getDataDir() + SystemUtils.SEP + "fgdb";
        return DataSourceUtils.init("jdbc:h2:file:" + db + ";MODE=MYSQL", "", "");
    }

}

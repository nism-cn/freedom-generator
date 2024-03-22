package org.nism.fg.base.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import org.nism.fg.base.core.CoreConstant;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JarsUtils {

    public static void loadJdbc() throws SQLException {
        List<File> jars = FileUtil.loopFiles(SystemUtils.getLibsDir(), path -> path.getName().toLowerCase().endsWith(".jar"));
        for (File jar : jars) {
            ClassLoader loader = ClassLoaderUtil.getJarClassLoader(jar);
            Map<String, DruidDataSource> beansOfType = SpringUtils.getBeanFactory().getBeansOfType(DruidDataSource.class);
            for (DruidDataSource ds : beansOfType.values()) {
                String driverClassName = JdbcUtils.getDriverClassName(ds.getUrl());
                if (JdbcConstants.H2_DRIVER.equals(driverClassName)) {
                    continue;
                }
                CoreConstant.JDBC_CLASS_LOADER_MAP.put(driverClassName, loader);
            }
        }
    }


}

package org.nism.fg.base.plugins;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.noear.solon.Solon;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Map.Entry;
import java.util.Properties;

public class FlywayPluginImpl implements Plugin {
    private static final Logger log = LoggerFactory.getLogger(FlywayPluginImpl.class);

    public FlywayPluginImpl() {
    }

    public void start(AppContext context) {
        Props props = context.cfg();
        props.loadAddIfAbsent("xm-flyway-solon-plugin.yml");
        context.subWrapsOfType(DataSource.class, (bean) -> {
            try {
                String named = bean.name();
                log.info("{} 异步订阅 DataSource({}), 执行 flyway 动作", "xm-flyway-solon-plugin", named);
                String keyStarts = "flyway." + named;
                boolean isEnabled = props.getBool(keyStarts + ".enable", true);
                if (isEnabled) {
                    Properties properties = new Properties();
                    Props flayWayProps = Solon.cfg().getProp(keyStarts);

                    for (Entry<Object, Object> objectObjectEntry : flayWayProps.entrySet()) {
                        String key = (String) objectObjectEntry.getKey();
                        Object value = objectObjectEntry.getValue();
                        if (!StrUtil.equalsIgnoreCase(key, "enable") && !StrUtil.containsAnyIgnoreCase(key, "-")) {
                            properties.put("flyway." + key, value);
                        }
                    }

                    FluentConfiguration fluentConfiguration = new FluentConfiguration();
                    fluentConfiguration.configuration(properties);
                    Flyway flyway = fluentConfiguration.dataSource(bean.raw()).load();
                    flyway.migrate();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Solon.stop();
            }
        });
        log.info("{} 包加载完毕!", "xm-flyway-solon-plugin");
    }

    public void stop() throws Throwable {
        log.info("{} 插件关闭!", "xm-flyway-solon-plugin");
    }
}

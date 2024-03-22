package org.nism.fg.base.config.runner;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.CoreConstant;
import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.domain.entity.DbInfo;
import org.nism.fg.service.DbInfoService;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.bean.LifecycleBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 初始化数据源
 *
 * @author inism
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class DatabaseInitRunner implements LifecycleBean {

    @Inject
    private DbInfoService dbInfoService;

    @Override
    public void start() throws Throwable {
        try {
            List<DbInfo> list = dbInfoService.list();
            log.info("{}", list);
            init(list);
            log.debug("数据源注入成功 : {}", list.stream().map(DbInfo::getName).collect(Collectors.toList()));
        } catch (Exception e) {
            // 忽略异常,项目正常启动
            log.error("数据源注入失败 ", e);
        }
    }

    private void init(List<DbInfo> list) {
        list.forEach(e -> {
            String id = CoreConstant.DB_BEAN_KEY + e.getId();
            DataSourceUtils.init(id, e.getJdbcUrl(), e.getUsername(), e.getPassword());
        });
    }

}
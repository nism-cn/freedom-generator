package org.nism.fg.base.runner;

import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.ds.GlobalDSFactory;
import cn.hutool.db.ds.hikari.HikariDSFactory;
import cn.hutool.setting.Setting;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.constant.CoreConstant;
import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.domain.entity.FgDatabaseInfo;
import org.nism.fg.service.FgDatabaseInfoService;
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
    private FgDatabaseInfoService dbInfoService;

    @Override
    public void start() throws Throwable {
        try {
            List<FgDatabaseInfo> list = dbInfoService.list();
            log.info("{}", list);
            init(list);
            log.debug("数据源注入成功 : {}", list.stream().map(FgDatabaseInfo::getName).collect(Collectors.toList()));
        } catch (Exception e) {
            // 忽略异常,项目正常启动
            log.error("数据源注入失败 ", e);
        }
    }

    private void init(List<FgDatabaseInfo> list) {
        list.forEach(e -> {
            String id = CoreConstant.DB_BEAN_KEY + e.getId();
            DataSourceUtils.init(id, e.getJdbcUrl(), e.getUsername(), e.getPassword());
        });
    }

}
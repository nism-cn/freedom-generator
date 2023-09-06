package org.nism.fg.base.runner;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.constant.CoreConstant;
import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.domain.entity.FgDatabaseInfo;
import org.nism.fg.service.FgDatabaseInfoService;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 初始化数据源
 *
 * @author inism
 * @since 1.0.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class DatabaseInitRunner implements CommandLineRunner {

    private final FgDatabaseInfoService dbInfoService;
    private final DefaultListableBeanFactory beanFactory;

    @Override
    public void run(String... args) {
        try {
            List<FgDatabaseInfo> list = dbInfoService.list();
            list.forEach(e -> beanFactory.registerSingleton(CoreConstant.DB_BEAN_KEY + e.getId(), DataSourceUtils.init(e)));
            log.debug("数据源注入成功 : {}", list.stream().map(FgDatabaseInfo::getName).collect(Collectors.toList()));
        } catch (Exception e) {
            // 忽略异常,项目正常启动
            log.error("数据源注入失败 ", e);
        }
    }

}
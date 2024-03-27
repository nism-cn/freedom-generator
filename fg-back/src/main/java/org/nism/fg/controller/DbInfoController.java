package org.nism.fg.controller;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.CoreConstant;
import org.nism.fg.base.core.R;
import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.domain.entity.DbInfo;
import org.nism.fg.service.DbInfoService;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

/**
 * 数据源控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("db-info")
public class DbInfoController {

    private final ConfigurableListableBeanFactory beanFactory;
    private final DbInfoService baseService;

    @GetMapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @GetMapping("{id}")
    public R<?> findOne(@PathVariable Serializable id) {
        return R.ok(baseService.getById(id));
    }

    @PostMapping
    public R<?> create(@RequestBody DbInfo e) {
        boolean save = baseService.save(e);
        String dsId = CoreConstant.DB_BEAN_KEY + e.getId();
        DataSource dataSource = DataSourceUtils.init(e);
        beanFactory.registerSingleton(dsId, dataSource);
        return R.ok(save);
    }

    @PutMapping("{id}")
    public R<?> update(@RequestBody DbInfo e, @PathVariable String id) throws SQLException {
        String dsId = CoreConstant.DB_BEAN_KEY + e.getId();
        DruidDataSource dataSource = beanFactory.getBean(dsId, DruidDataSource.class);
        dataSource.restart();
        boolean update = baseService.updateById(e);
        DbInfo byId = baseService.getById(id);
        dataSource.setUrl(byId.getJdbcUrl());
        dataSource.setUsername(byId.getUsername());
        dataSource.setPassword(byId.getPassword());
        return R.ok(update);
    }

    @GetMapping("dataSources")
    public R<?> dataSources() {
        Map<String, DataSource> beansOfType = beanFactory.getBeansOfType(DataSource.class);
        return R.ok(beansOfType.keySet());
    }

    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable String id) {
        return R.ok(baseService.removeById(id));
    }

    @PostMapping("test-connection")
    public R<?> testConnection(@RequestBody DbInfo e) {
        DataSourceUtils.test(e);
        return R.ok();
    }

}

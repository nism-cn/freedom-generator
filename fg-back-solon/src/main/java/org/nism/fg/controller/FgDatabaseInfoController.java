package org.nism.fg.controller;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.constant.CoreConstant;
import org.nism.fg.base.core.R;
import org.nism.fg.base.utils.Assert;
import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.domain.entity.FgDatabaseInfo;
import org.nism.fg.service.FgDatabaseInfoService;
import org.noear.solon.Solon;
import org.noear.solon.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 数据源控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@Controller
@Mapping("database-info")
public class FgDatabaseInfoController {

    //    private ConfigurableListableBeanFactory beanFactory;
    @Inject
    private FgDatabaseInfoService baseService;

    @Mapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @Mapping("{id}")
    public R<?> findOne(Long id) {
        return R.ok(baseService.getById(id));
    }

    @Post
    @Mapping
    public R<?> create(FgDatabaseInfo e) {
        boolean save = baseService.save(e);
        String dsId = CoreConstant.DB_BEAN_KEY + e.getId();
        DataSource dataSource = DataSourceUtils.init(e);
//        beanFactory.registerSingleton(dsId, dataSource);
        Solon.context().wrap(dsId, dataSource);
//        Solon.context().wrapAndPut(UserService.class, new UserServiceImpl());

        return R.ok(save);
    }

    @Put
    @Mapping("{id}")
    public R<?> update(FgDatabaseInfo e, String id) throws SQLException {
        boolean update = baseService.updateById(e);
        Assert.isTrue(update, "更新失败!");
        System.out.println(e);
        DataSourceUtils.init(e);
        FgDatabaseInfo byId = baseService.getById(id);
//        dataSource.setUrl(byId.getJdbcUrl());
//        dataSource.setUsername(byId.getUsername());
//        dataSource.setPassword(byId.getPassword());
        return R.ok(update);
    }

    @Mapping("dataSources")
    public R<?> dataSources() {
//        Map<String, DataSource> beansOfType = beanFactory.getBeansOfType(DataSource.class);
//        return R.ok(beansOfType.keySet());
        return R.ok();
    }

    @Delete
    @Mapping("{id}")
    public R<?> delete(String id) {
        return R.ok(baseService.removeById(id));
    }

    @Post
    @Mapping("test-connection")
    public R<?> testConnection(FgDatabaseInfo e) throws SQLException {
        DataSourceUtils.test(e);
        return R.ok();
    }

}

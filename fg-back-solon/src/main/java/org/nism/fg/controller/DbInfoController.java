package org.nism.fg.controller;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.CoreConstant;
import org.nism.fg.base.core.R;
import org.nism.fg.base.utils.Assert;
import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.domain.entity.DbInfo;
import org.nism.fg.service.DbInfoService;
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
public class DbInfoController {

    @Inject
    private DbInfoService baseService;

    @Get
    @Mapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @Get
    @Mapping("{id}")
    public R<?> findOne(Long id) {
        return R.ok(baseService.getById(id));
    }

    @Post
    @Mapping
    public R<?> create(DbInfo e) {
        boolean save = baseService.save(e);
        String dsId = CoreConstant.DB_BEAN_KEY + e.getId();
        DataSource dataSource = DataSourceUtils.init(e);
        return R.ok(save);
    }

    @Put
    @Mapping("{id}")
    public R<?> update(DbInfo e, Long id) throws SQLException {
        e.setId(id);
        boolean update = baseService.updateById(e);
        Assert.isTrue(update, "更新失败!");
        DataSourceUtils.init(e);
        return R.ok();
    }

    @Delete
    @Mapping("{id}")
    public R<?> delete(String id) {
        return R.ok(baseService.removeById(id));
    }

    @Post
    @Mapping("test-connection")
    public R<?> testConnection(DbInfo e) throws SQLException {
        DataSourceUtils.test(e);
        return R.ok();
    }

}

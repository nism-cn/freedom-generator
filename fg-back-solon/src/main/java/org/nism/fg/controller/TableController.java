package org.nism.fg.controller;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.I2;
import org.nism.fg.base.core.R;
import org.nism.fg.base.utils.PageUtils;
import org.nism.fg.base.utils.ServletUtils;
import org.nism.fg.domain.entity.Table;
import org.nism.fg.domain.entity.Column;
import org.nism.fg.service.TableService;
import org.noear.solon.annotation.*;

import java.util.List;

/**
 * 生成表控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@Controller
@Mapping("table")
public class TableController {

    @Inject
    private TableService baseService;

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
    public R<?> create(Table e) {
        return R.ok(baseService.save(e));
    }

    @Put
    @Mapping("{id}")
    public R<?> update(Table e, Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @Delete
    @Mapping("{id}")
    public R<?> delete(Long id) {
        return R.ok(baseService.removeById(id));
    }

    @Delete
    @Mapping("batch/{ids}")
    public R<?> deleteBatch(List<Long> ids) {
        return R.ok(baseService.removeByIds(ids));
    }

    @Get
    @Mapping("project/{projectId}")
    public R<?> findByProjectId(Long projectId, @Body String search) {
        return R.ok(PageUtils.startPage().doSelectPage(() -> baseService.lambdaQuery()
                .eq(Table::getProjectId, projectId)
                .and(StrUtil.isNotBlank(search), e -> {
                    e.like(Table::getName, search);
                    e.or();
                    e.like(Table::getComment, search);
                }).list()
        ));
    }

    /**
     * 获取数据库表
     */
    @Get
    @Mapping("db-tables/{settingId}")
    private R<?> dbTables(Long settingId) {
        return R.ok(baseService.getDbTables(settingId));
    }

    /**
     * 获取字典数据
     */
    @Get
    @Mapping("dictList/{tableId}")
    private R<?> dictList(Long tableId) {
        return R.ok(baseService.dictList(tableId));
    }

    /**
     * 修改生成配置
     */
    @Put
    @Mapping("modify")
    public R<?> modify(I2<Table, List<Column>> data) {
        baseService.modify(data.getData(), data.getSub());
        return R.ok();
    }

    /**
     * 预览代码
     */
    @Get
    @Mapping("preview/{id}")
    public R<?> preview(Long id) throws Exception {
        return R.ok(baseService.preview(id));
    }

    /**
     * 生成代码
     */
    @Get
    @Mapping("generator/{ids}")
    public void generator(List<String> ids) throws Exception {
        ServletUtils.byteDownload(baseService.generator(ids));
    }

}

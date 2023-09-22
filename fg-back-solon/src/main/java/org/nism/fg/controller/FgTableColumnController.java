package org.nism.fg.controller;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.FgTableColumn;
import org.nism.fg.service.FgTableColumnService;
import org.noear.solon.annotation.*;


/**
 * 生成表字段控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@Controller
@Mapping("table-column")
public class FgTableColumnController {

    @Inject
    private FgTableColumnService baseService;

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
    public R<?> create(FgTableColumn e) {
        return R.ok(baseService.save(e));
    }

    @Put
    @Mapping("{id}")
    public R<?> update(FgTableColumn e, Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @Delete
    @Mapping("{id}")
    public R<?> delete(Long id) {
        return R.ok(baseService.removeById(id));
    }

    @Mapping("tableId/{tableId}")
    public R<?> find(Long tableId) {
        return R.ok(baseService.lambdaQuery().eq(FgTableColumn::getTableId, tableId).list());
    }


}

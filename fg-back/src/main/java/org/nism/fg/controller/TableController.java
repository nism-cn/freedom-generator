package org.nism.fg.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.mvc.controller.BaseController;
import org.nism.fg.base.core.mvc.domain.BaseEntity;
import org.nism.fg.base.core.mvc.domain.I2;
import org.nism.fg.base.core.mvc.domain.R;
import org.nism.fg.base.utils.PageUtils;
import org.nism.fg.base.utils.ServletUtils;
import org.nism.fg.domain.entity.Column;
import org.nism.fg.domain.entity.Table;
import org.nism.fg.service.TableService;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 生成表控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("table")
public class TableController extends BaseController<TableService, Table> {

    @DeleteMapping("batch/{ids}")
    public R<?> deleteBatch(@PathVariable List<Serializable> ids) {
        return R.ok(baseService.removeByIds(ids));
    }

    @GetMapping("project/{projectId}")
    public R<?> findByProjectId(@PathVariable Serializable projectId, String search) {
        return R.ok(PageUtils.startPage().doSelectPage(() -> baseService.lambdaQuery()
                .eq(Table::getProjectId, projectId)
                .and(StrUtil.isNotBlank(search), e -> {
                    e.like(Table::getName, search);
                    e.or();
                    e.like(Table::getComment, search);
                })
                .orderByDesc(BaseEntity::getId)
                .list()
        ));
    }

    /**
     * 获取数据库表
     */
    @GetMapping("db-tables/{setsId}")
    private R<?> dbTables(@PathVariable Serializable setsId) {
        return R.ok(baseService.getDbTables(setsId));
    }

    /**
     * 获取字典数据
     */
    @GetMapping("dictList/{tableId}")
    private R<?> dictList(@PathVariable Serializable tableId) {
        return R.ok(baseService.dictList(tableId));
    }

    /**
     * 修改生成配置
     */
    @PutMapping("modify")
    public R<?> modify(@RequestBody I2<Table, List<Column>> data) {
        baseService.modify(data.getData(), data.getSub());
        return R.ok();
    }

    /**
     * 预览代码
     */
    @GetMapping("preview/{id}")
    public R<?> preview(@PathVariable Serializable id) throws Exception {
        return R.ok(baseService.preview(id));
    }

    /**
     * 生成代码
     */
    @GetMapping("generator/{ids}")
    public void generator(@PathVariable List<String> ids) throws Exception {
        ServletUtils.byteDownload(baseService.generator(ids));
    }

}

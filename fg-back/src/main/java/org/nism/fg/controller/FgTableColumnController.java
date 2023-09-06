package org.nism.fg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.FgTableColumn;
import org.nism.fg.service.FgTableColumnService;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 生成表字段控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("table-column")
public class FgTableColumnController {

    private final FgTableColumnService baseService;

    @GetMapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @GetMapping("{id}")
    public R<?> findOne(@PathVariable Serializable id) {
        return R.ok(baseService.getById(id));
    }

    @PostMapping
    public R<?> create(@RequestBody FgTableColumn e) {
        return R.ok(baseService.save(e));
    }

    @PutMapping("{id}")
    public R<?> update(@RequestBody FgTableColumn e, @PathVariable String id) {
        return R.ok(baseService.updateById(e));
    }

    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable Serializable id) {
        return R.ok(baseService.removeById(id));
    }

    @GetMapping("tableId/{tableId}")
    public R<?> find(@PathVariable Serializable tableId) {
        return R.ok(baseService.lambdaQuery().eq(FgTableColumn::getTableId, tableId).list());
    }


}

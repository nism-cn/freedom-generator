package org.nism.fg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.mvc.controller.BaseController;
import org.nism.fg.base.core.mvc.domain.R;
import org.nism.fg.domain.entity.Column;
import org.nism.fg.service.ColumnService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("column")
public class ColumnController extends BaseController<ColumnService, Column> {

    @GetMapping("tableId/{tableId}")
    public R<?> find(@PathVariable Serializable tableId) {
        return R.ok(baseService.lambdaQuery().eq(Column::getTableId, tableId).list());
    }

}

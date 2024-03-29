package org.nism.fg.base.core.mvc.controller;

import com.github.pagehelper.Page;
import org.nism.fg.base.core.mvc.domain.BaseEntity;
import org.nism.fg.base.core.mvc.domain.R;
import org.nism.fg.base.core.mvc.service.IService;
import org.nism.fg.base.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.sql.SQLException;


public class BaseController<S extends IService<T>, T extends BaseEntity> {

    @Autowired
    protected S baseService;

    @GetMapping("/find")
    public Page<?> page(T e) {
        return PageUtils.startPage().doSelectPage(() -> baseService.find(e));
    }

    @GetMapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @GetMapping("{id}")
    public R<?> findOne(@PathVariable Serializable id) {
        return R.ok(baseService.getById(id));
    }

    @PostMapping
    public R<?> create(@RequestBody T e) {
        boolean save = baseService.save(e);
        return R.ok(save);
    }

    @PutMapping("{id}")
    public R<?> update(@RequestBody @Validated T e, @PathVariable String id) throws SQLException {
        boolean update = baseService.updateById(e);
        return R.ok(update);
    }

    @PostMapping("saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody @Validated T e) {
        return R.ok(baseService.saveOrUpdate(e));
    }


    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable String id) {
        return R.ok(baseService.removeById(id));
    }


}

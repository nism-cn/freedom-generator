package org.nism.fg.base.core.mvc.controller;

import org.nism.fg.base.core.mvc.domain.R;
import org.nism.fg.base.core.mvc.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.sql.SQLException;


public class BaseController<S extends IService<T>, T> {

    @Autowired
    protected S baseService;

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

    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable String id) {
        return R.ok(baseService.removeById(id));
    }


}

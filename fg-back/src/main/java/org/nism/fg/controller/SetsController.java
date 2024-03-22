package org.nism.fg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.service.SetsService;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 项目设置控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("project-setting")
public class SetsController {

    private final SetsService baseService;

    @GetMapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @GetMapping("{id}")
    public R<?> findOne(@PathVariable Serializable id) {
        return R.ok(baseService.getById(id));
    }

    @PostMapping
    public R<?> create(@RequestBody Sets e) {
        baseService.save(e);
        return R.ok(e);
    }

    @PutMapping("{id}")
    public R<?> update(@RequestBody Sets e, @PathVariable Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @PostMapping("saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody Sets e) {
        baseService.saveOrUpdate(e);
        return R.ok(e);
    }


    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable String id) {
        return R.ok(baseService.removeById(id));
    }


}

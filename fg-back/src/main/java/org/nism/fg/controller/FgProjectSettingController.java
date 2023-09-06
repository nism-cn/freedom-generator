package org.nism.fg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.FgProjectSetting;
import org.nism.fg.service.FgProjectSettingService;
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
public class FgProjectSettingController {

    private final FgProjectSettingService baseService;

    @GetMapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @GetMapping("{id}")
    public R<?> findOne(@PathVariable Serializable id) {
        return R.ok(baseService.getById(id));
    }

    @PostMapping
    public R<?> create(@RequestBody FgProjectSetting e) {
        baseService.save(e);
        return R.ok(e);
    }

    @PutMapping("{id}")
    public R<?> update(@RequestBody FgProjectSetting e, @PathVariable String id) {
        return R.ok(baseService.updateById(e));
    }

    @PostMapping("saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody FgProjectSetting e) {
        baseService.saveOrUpdate(e);
        return R.ok(e);
    }


    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable String id) {
        return R.ok(baseService.removeById(id));
    }


}

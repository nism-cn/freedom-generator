package org.nism.fg.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.Type;
import org.nism.fg.service.TypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 类型控制层
 *
 * @author nism
 * @since 1.0.1
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("type")
public class TypeController {

    private final TypeService baseService;

    @GetMapping("list/{mold}")
    public R<?> listMold(@PathVariable String mold, String search, boolean dis) {
        return R.ok(baseService.lambdaQuery()
                .eq(Type::getMold, mold)
                .like(StrUtil.isNotBlank(search), Type::getVal, search)
                .orderByDesc(BaseEntity::getId)
                .eq(Type::getDis, dis)
                .list()
        );
    }

    @GetMapping
    public R<?> find() {
        return R.ok(baseService.lambdaQuery().eq(Type::getDis, false).list());
    }

    @GetMapping("{id}")
    public R<?> findOne(@PathVariable Serializable id) {
        return R.ok(baseService.lambdaQuery().eq(BaseEntity::getId, id).eq(Type::getDis, false).one());
    }

    @PostMapping
    public R<?> create(@RequestBody @Validated Type e) {
        return R.ok(baseService.save(e));
    }

    @PutMapping("{id}")
    public R<?> update(@RequestBody @Validated Type e, @PathVariable Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @PutMapping("{id}/{dis}")
    public R<?> updateDis(@PathVariable Long id, @PathVariable Boolean dis) {
        return R.ok(baseService.lambdaUpdate().eq(BaseEntity::getId, id).set(Type::getDis, dis).update());
    }

    @PostMapping("saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody @Validated Type e) {
        return R.ok(baseService.saveOrUpdate(e));
    }

    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable Serializable id) {
        return R.ok(baseService.removeById(id));
    }

}

package org.nism.fg.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.FgType;
import org.nism.fg.service.FgTypeService;
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
public class FgTypeController {

    private final FgTypeService baseService;

    @GetMapping("list/{mold}")
    public R<?> listMold(@PathVariable String mold, String search, boolean dis) {
        return R.ok(baseService.lambdaQuery()
                .eq(FgType::getMold, mold)
                .like(StrUtil.isNotBlank(search), FgType::getVal, search)
                .orderByDesc(BaseEntity::getId)
                .eq(FgType::getDis, dis)
                .list()
        );
    }

    @GetMapping
    public R<?> find() {
        return R.ok(baseService.lambdaQuery().eq(FgType::getDis, false).list());
    }

    @GetMapping("{id}")
    public R<?> findOne(@PathVariable Serializable id) {
        return R.ok(baseService.lambdaQuery().eq(BaseEntity::getId, id).eq(FgType::getDis, false).one());
    }

    @PostMapping
    public R<?> create(@RequestBody FgType e) {
        return R.ok(baseService.save(e));
    }

    @PutMapping("{id}")
    public R<?> update(@RequestBody FgType e, @PathVariable Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @PostMapping("saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody FgType e) {
        return R.ok(baseService.saveOrUpdate(e));
    }

    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable Serializable id) {
        return R.ok(baseService.removeById(id));
    }

}

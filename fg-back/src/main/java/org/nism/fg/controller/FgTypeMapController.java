package org.nism.fg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.FgType;
import org.nism.fg.domain.entity.FgTypeMap;
import org.nism.fg.service.FgTypeMapService;
import org.nism.fg.service.FgTypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类型映射控制层
 *
 * @author nism
 * @since 1.0.1
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("type-map")
public class FgTypeMapController {

    private final FgTypeService typeService;
    private final FgTypeMapService baseService;

    @GetMapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @GetMapping("list/{typeMold}/{mapMold}")
    public R<?> listMold(@PathVariable String typeMold, @PathVariable String mapMold) {
        List<FgTypeMap> typeMaps = baseService.lambdaQuery()
                .eq(FgTypeMap::getTypeMold, typeMold)
                .eq(FgTypeMap::getMapMold, mapMold)
                .orderByDesc(BaseEntity::getId)
                .list();

        Map<Long, String> maps = typeService.list().stream().collect(Collectors.toMap(BaseEntity::getId, FgType::getVal));

        for (FgTypeMap tm : typeMaps) {
            tm.setTypeVal(maps.get(tm.getTypeId()));
            tm.setMapVal(maps.get(tm.getMapId()));
        }
        return R.ok(typeMaps);
    }

    @GetMapping("{id}")
    public R<?> findOne(@PathVariable Serializable id) {
        return R.ok(baseService.getById(id));
    }

    @PostMapping
    public R<?> create(@RequestBody @Validated FgTypeMap e) {
        return R.ok(baseService.save(e));
    }

    @PutMapping("{id}")
    public R<?> update(@RequestBody @Validated FgTypeMap e, @PathVariable Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @PostMapping("saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody @Validated FgTypeMap e) {
        return R.ok(baseService.saveOrUpdate(e));
    }

    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable Serializable id) {
        return R.ok(baseService.removeById(id));
    }

    @GetMapping("groups")
    public R<?> groups() {
        return R.ok(baseService.lambdaQuery()
                .select(FgTypeMap::getTypeMold, FgTypeMap::getMapMold)
                .groupBy(FgTypeMap::getTypeMold, FgTypeMap::getMapMold)
                .list()
        );
    }

}

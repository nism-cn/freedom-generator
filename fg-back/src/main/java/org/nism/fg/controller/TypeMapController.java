package org.nism.fg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.Type;
import org.nism.fg.domain.entity.TypeMap;
import org.nism.fg.service.TypeMapService;
import org.nism.fg.service.TypeService;
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
public class TypeMapController {

    private final TypeService typeService;
    private final TypeMapService baseService;

    @GetMapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @GetMapping("list/{typeMold}/{mapMold}")
    public R<?> listMold(@PathVariable String typeMold, @PathVariable String mapMold) {
        List<TypeMap> typeMaps = baseService.lambdaQuery()
                .eq(TypeMap::getTypeMold, typeMold)
                .eq(TypeMap::getMapMold, mapMold)
                .orderByDesc(BaseEntity::getId)
                .list();

        Map<Long, String> maps = typeService.list().stream().collect(Collectors.toMap(BaseEntity::getId, Type::getVal));

        for (TypeMap tm : typeMaps) {
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
    public R<?> create(@RequestBody @Validated TypeMap e) {
        return R.ok(baseService.save(e));
    }

    @PutMapping("{id}")
    public R<?> update(@RequestBody @Validated TypeMap e, @PathVariable Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @PostMapping("saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody @Validated TypeMap e) {
        return R.ok(baseService.saveOrUpdate(e));
    }

    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable Serializable id) {
        return R.ok(baseService.removeById(id));
    }

    @GetMapping("groups")
    @SuppressWarnings({"unchecked"})
    public R<?> groups() {
        return R.ok(baseService.lambdaQuery()
                .select(TypeMap::getTypeMold, TypeMap::getMapMold)
                .groupBy(TypeMap::getTypeMold, TypeMap::getMapMold)
                .list()
        );
    }

}

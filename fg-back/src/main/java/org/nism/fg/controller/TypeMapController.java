package org.nism.fg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.mvc.controller.BaseController;
import org.nism.fg.base.core.mvc.domain.BaseEntity;
import org.nism.fg.base.core.mvc.domain.R;
import org.nism.fg.domain.entity.Type;
import org.nism.fg.domain.entity.TypeMap;
import org.nism.fg.service.TypeMapService;
import org.nism.fg.service.TypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class TypeMapController extends BaseController<TypeMapService, TypeMap> {

    private final TypeService typeService;

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

    @PostMapping("saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody @Validated TypeMap e) {
        return R.ok(baseService.saveOrUpdate(e));
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

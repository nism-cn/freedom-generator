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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public R<?> listMold(@PathVariable String typeMold, @PathVariable String mapMold, String search) {
        List<TypeMap> typeMaps = baseService.lambdaQuery()
                .eq(TypeMap::getTypeMold, typeMold)
                .eq(TypeMap::getMapMold, mapMold)
                .orderByDesc(TypeMap::getId)
                .list();

        Map<Long, String> maps = typeService.list().stream().collect(Collectors.toMap(BaseEntity::getId, Type::getVal));

        for (TypeMap tm : typeMaps) {
            tm.setTypeVal(maps.get(tm.getTypeId()));
            tm.setMapVal(maps.get(tm.getMapId()));
        }
        return R.ok(typeMaps);
    }

    @GetMapping("groups")
    public R<?> groups() {
        return R.ok(baseService.query()
                .select("TYPE_MOLD", "MAP_MOLD")
                .groupBy("TYPE_MOLD", "MAP_MOLD")
                .orderByAsc("MAX(ID)")
                .list()
        );
    }

}

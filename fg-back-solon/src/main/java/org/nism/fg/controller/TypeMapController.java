package org.nism.fg.controller;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.Type;
import org.nism.fg.domain.entity.TypeMap;
import org.nism.fg.service.TypeMapService;
import org.nism.fg.service.TypeService;
import org.noear.solon.annotation.*;

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
@Controller
@Mapping("type-map")
public class TypeMapController {

    @Inject
    private TypeService typeService;
    @Inject
    private TypeMapService baseService;

    @Get
    @Mapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @Get
    @Mapping("list/{typeMold}/{mapMold}")
    public R<?> listMold(String typeMold, String mapMold) {
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

    @Get
    @Mapping("{id}")
    public R<?> findOne(Long id) {
        return R.ok(baseService.getById(id));
    }

    @Post
    @Mapping
    public R<?> create(TypeMap e) {
        return R.ok(baseService.save(e));
    }

    @Put
    @Mapping("{id}")
    public R<?> update(TypeMap e, Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @Post
    @Mapping("saveOrUpdate")
    public R<?> saveOrUpdate(TypeMap e) {
        return R.ok(baseService.saveOrUpdate(e));
    }

    @Delete
    @Mapping("{id}")
    public R<?> delete(Long id) {
        return R.ok(baseService.removeById(id));
    }

    @Get
    @Mapping("groups")
    @SuppressWarnings({"unchecked"})
    public R<?> groups() {
        return R.ok(baseService.lambdaQuery()
                .select(TypeMap::getTypeMold, TypeMap::getMapMold)
                .groupBy(TypeMap::getTypeMold, TypeMap::getMapMold)
                .list()
        );
    }

}

package org.nism.fg.controller;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.FgType;
import org.nism.fg.domain.entity.FgTypeMap;
import org.nism.fg.service.FgTypeMapService;
import org.nism.fg.service.FgTypeService;
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
public class FgTypeMapController {

    @Inject
    private FgTypeService typeService;
    @Inject
    private FgTypeMapService baseService;

    @Get
    @Mapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @Get
    @Mapping("list/{typeMold}/{mapMold}")
    public R<?> listMold(String typeMold, String mapMold) {
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

    @Get
    @Mapping("{id}")
    public R<?> findOne(Long id) {
        return R.ok(baseService.getById(id));
    }

    @Post
    @Mapping
    public R<?> create(FgTypeMap e) {
        return R.ok(baseService.save(e));
    }

    @Put
    @Mapping("{id}")
    public R<?> update(FgTypeMap e, Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @Post
    @Mapping("saveOrUpdate")
    public R<?> saveOrUpdate(FgTypeMap e) {
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
                .select(FgTypeMap::getTypeMold, FgTypeMap::getMapMold)
                .groupBy(FgTypeMap::getTypeMold, FgTypeMap::getMapMold)
                .list()
        );
    }

}

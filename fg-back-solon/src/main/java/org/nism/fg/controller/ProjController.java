package org.nism.fg.controller;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.I1;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.dto.MapDTO;
import org.nism.fg.domain.entity.Proj;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.service.ProjService;
import org.nism.fg.service.SetsService;
import org.nism.fg.service.TypeService;
import org.noear.solon.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 项目控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@Controller
@Mapping("project")
public class ProjController {

    @Inject
    private TypeService typeService;
    @Inject
    private ProjService baseService;
    @Inject
    private SetsService setsService;

    @Get
    @Mapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @Get
    @Mapping("{id}")
    public R<?> findOne(Long id) {
        return R.ok(baseService.getById(id));
    }

    @Post
    @Mapping
    public R<?> create(Proj e) {
        baseService.save(e);
        return R.ok(e);
    }

    @Put
    @Mapping("{id}")
    public R<?> update(Proj e, Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @Delete
    @Mapping("{id}")
    public R<?> delete(String id) {
        return R.ok(baseService.removeById(id));
    }

    @Mapping("setting/{id}")
    public R<?> findSetting(Long id) {
        Sets one = setsService.lambdaQuery().eq(Sets::getProjectId, id).one();
        return R.ok(Optional.ofNullable(one).orElse(new Sets()));
    }

    @Post
    @Mapping("importTables/{projectId}")
    public R<?> importTables(Long projectId, I1<List<String>> tables) {
        baseService.importTables(projectId, tables.getData());
        return R.ok();
    }

    @Get
    @Mapping("types")
    private R<?> types() {
        Map<String, Object> data = new HashMap<>();
        MapDTO map = typeService.loadMap();
        // base type
        data.put("javaType", map.getJavaType());
        data.put("columnType", map.getColumnType());
        data.put("htmlType", map.getHtmlType());
        data.put("selectType", map.getSelectType());
        // mappings
        data.put("columnTypeMap", map.getColumnTypeMap());
        data.put("htmlTypeMap", map.getHtmlTypeMap());
        return R.ok(data);
    }

}

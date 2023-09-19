package org.nism.fg.controller;

import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.I1;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.dto.MapDTO;
import org.nism.fg.domain.entity.FgProject;
import org.nism.fg.domain.entity.FgProjectSetting;
import org.nism.fg.service.FgProjectService;
import org.nism.fg.service.FgProjectSettingService;
import org.nism.fg.service.FgTypeService;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
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
@RestController
@RequiredArgsConstructor
@RequestMapping("project")
public class FgProjectController {

    private final Configuration cfg;
    private final FgTypeService typeService;
    private final FgProjectService baseService;
    private final FgProjectSettingService projectSettingService;

    @GetMapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @GetMapping("{id}")
    public R<?> findOne(@PathVariable Serializable id) {
        return R.ok(baseService.getById(id));
    }

    @PostMapping
    public R<?> create(@RequestBody FgProject e) {
        baseService.save(e);
        return R.ok(e);
    }

    @PutMapping("{id}")
    public R<?> update(@RequestBody FgProject e, @PathVariable Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @DeleteMapping("{id}")
    public R<?> delete(@PathVariable String id) {
        return R.ok(baseService.removeById(id));
    }

    @GetMapping("setting/{id}")
    public R<?> findSetting(@PathVariable Serializable id) {
        FgProjectSetting one = projectSettingService.lambdaQuery().eq(FgProjectSetting::getProjectId, id).one();
        return R.ok(Optional.ofNullable(one).orElse(new FgProjectSetting()));
    }

    @PostMapping("importTables/{projectId}")
    public R<?> importTables(@PathVariable Serializable projectId, @RequestBody I1<List<String>> tables) {
        baseService.importTables(projectId, tables.getData());
        return R.ok();
    }

    @GetMapping("types")
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

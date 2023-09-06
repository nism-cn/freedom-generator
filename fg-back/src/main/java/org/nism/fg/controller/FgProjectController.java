package org.nism.fg.controller;

import cn.hutool.core.io.IoUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.config.props.MappingProp;
import org.nism.fg.base.core.I1;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.FgProject;
import org.nism.fg.domain.entity.FgProjectSetting;
import org.nism.fg.service.FgProjectService;
import org.nism.fg.service.FgProjectSettingService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

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
    private final MappingProp mappingProp;
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
        // base type
        data.put("javaType", mappingProp.getJavaType());
        data.put("columnType", mappingProp.getColumnType());
        data.put("htmlType", mappingProp.getHtmlType());
        data.put("selectType", mappingProp.getSelectType());
        // mappings
        data.put("columnTypeMap", mappingProp.getColumnTypeMap());
        data.put("htmlTypeMap", mappingProp.getHtmlTypeMap());
        return R.ok(data);
    }

    /**
     * 开始渲染
     */
    @PostMapping("process")
    public R<?> process(@RequestBody I1<String> data) throws IOException, TemplateException {
        String path = data.getData();
        // 增加灵魂
        Map<String, Object> root = new HashMap<>();
        root.put("packageName", "org.nism.gen");
        root.put("ClassName", "Demo");
        root.put("className", "demo");
        root.put("comment", "演示样例");
        root.put("author", "nism");
        root.put("moduleName", "demo-module");
        root.put("businessName", "demo-business");

        root.put("tableName", "gen-table");
        root.put("license", "The MIT License (MIT)");
        Map<String, Object> pkColumn = new HashMap<>();
        root.put("pkColumn", pkColumn);

        pkColumn.put("javaField", "id");
        pkColumn.put("javaType", "Long");
        pkColumn.put("comment", "主键不解释");

        List<Map<String, Object>> columns = new ArrayList<>();
        root.put("columns", columns);
        columns.add(pkColumn);
        columns.add(pkColumn);

        // 获取模板数据
        Template temp = cfg.getTemplate(path);

        Writer sw = new StringWriter();
        temp.process(root, sw);
        String r = sw.toString();
        IoUtil.close(sw);

        return R.ok(r);

    }
}

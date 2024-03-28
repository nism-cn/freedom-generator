package org.nism.fg.base.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.nism.fg.base.core.BaseConstant;
import org.nism.fg.domain.convert.FileConvert;
import org.nism.fg.domain.dto.FileDTO;
import org.nism.fg.domain.entity.Column;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.domain.entity.Table;
import org.nism.fg.service.TypeService;
import org.springframework.util.Assert;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代码生成器 工具类
 *
 * @author inism
 * @since 1.0.0
 */
public class GenUtils {

    private GenUtils() {
    }

    /**
     * 初始化表信息
     */
    public static Table buildTable(cn.hutool.db.meta.Table t) {
        Table table = new Table();
        String tableName = t.getTableName().toLowerCase();
        table.setName(t.getTableName());
        table.setComment(t.getComment());
        table.setCamel(StrUtil.toCamelCase(tableName));
        table.setUpCamel(StrUtil.upperFirst(StrUtil.toCamelCase(tableName)));
        table.setUnderline(StrUtil.toUnderlineCase(tableName).toLowerCase());
        table.setUpUnderline(StrUtil.toUnderlineCase(tableName).toUpperCase());
        return table;
    }

    /**
     * 初始化列属性字段
     */
    public static Column buildColumn(cn.hutool.db.meta.Column c) {
        String columnName = c.getName().toLowerCase();

        Column column = new Column();

        // 数据库原有属性
        column.setName(c.getName());
        column.setComment(c.getComment());
        column.setType(c.getTypeName());
        column.setPk(c.isPk());
        column.setIncrement(c.isAutoIncrement());
        column.setNullable(c.isNullable());
        column.setRequired(!c.isNullable());
        column.setSize(c.getSize());
        column.setDigit(c.getDigit());

        column.setCamel(StrUtil.toCamelCase(columnName));
        column.setUpCamel(StrUtil.upperFirst(StrUtil.toCamelCase(columnName)));
        column.setUnderline(StrUtil.toUnderlineCase(columnName).toLowerCase());
        column.setUpUnderline(StrUtil.toUnderlineCase(columnName).toUpperCase());

        final TypeService typeService = SpringUtils.getBean(TypeService.class);
        column.setTypes(typeService.loadMaps(column.getType()));

        return column;
    }

    public static Map<String, Object> buildTemplateData(Table table) {
        Sets sets = table.getSets();
        Assert.notNull(sets, "未找到项目配置信息,请先配置项目!");
        final String rootPath = SystemUtils.getTemplateDir() + SystemUtils.SEP + sets.getTempPath();
        Map<String, Object> root = new HashMap<>();

        // 获取模板
        List<File> tempFiles = FileUtil.loopFiles(rootPath, path -> path.getName().toLowerCase().endsWith(".ftl"));
        Assert.notEmpty(tempFiles, "未找到模板!");
        List<FileDTO> tempFileDtoList = tempFiles.stream().map(FileConvert::to).collect(Collectors.toList());

        // 注入freemarker 参数
        root.put("table", table);
        root.put("columns", table.getColumns());
        List<Column> pkColumns = table.getColumns().stream().filter(Column::getPk).collect(Collectors.toList());
        // 表无主键情况
        root.put("pkColumn", pkColumns.isEmpty() ? null : pkColumns.get(0));
        root.put("pkColumns", pkColumns);
        root.put("sets", table.getSets());
        root.put(BaseConstant.DTO_KEY, tempFileDtoList);

        // 获取参数
        String argsPath = rootPath + SystemUtils.SEP + SystemUtils.ARGS;
        if (FileUtil.exist(argsPath)) {
            String json = FileUtil.readUtf8String(argsPath);
            root.put("args", JsonUtils.toObject(json, Object.class));
        }
        return root;
    }

}

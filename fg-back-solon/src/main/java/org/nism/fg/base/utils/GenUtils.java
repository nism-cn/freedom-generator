package org.nism.fg.base.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import freemarker.core.Environment;
import org.nism.fg.base.config.props.RootDirProp;
import org.nism.fg.base.core.CoreConstant;
import org.nism.fg.base.core.adapter.TypeAdapter;
import org.nism.fg.domain.convert.FileConvert;
import org.nism.fg.domain.dto.FileDTO;
import org.nism.fg.domain.dto.MapDTO;
import org.nism.fg.domain.entity.Column;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.domain.entity.Table;
import org.nism.fg.service.TypeService;
import org.noear.solon.Solon;

import java.io.File;
import java.io.StringReader;
import java.util.*;
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
    public static Table buildTable(cn.hutool.db.meta.Table dbTable, Sets sets) {
        Table table = new Table();

        table.setProjectId(sets.getProjectId());

        table.setName(dbTable.getTableName());
        table.setComment(dbTable.getComment());

        table.setClassName(convertClassName(dbTable.getTableName(), sets.getIgnoreTablePrefix()));
        table.setRootPackage(sets.getRootPackage());
        table.setModuleName(getLast(sets.getRootPackage(), "."));
//        table.setBusinessName(getLast(dbTable.getTableName(), "_"));
        table.setBusinessName(table.getClassName());
        table.setAuthor(sets.getAuthor());

        return table;
    }

    /**
     * 初始化列属性字段
     */
    public static Column buildColumn(cn.hutool.db.meta.Column dbColumn, Sets sets) {
        String columnName = dbColumn.getName();

        // 忽略生成的字段
        Set<String> ignoreColumnSet = new HashSet<>(StrUtil.split(sets.getIgnoreColumn(), ","));
        if (ignoreColumnSet.contains(columnName)) {
            return null;
        }

        Column column = new Column();
        column.setProjectId(sets.getProjectId());

        // 数据库原有属性
        column.setName(dbColumn.getName());
        column.setComment(dbColumn.getComment());
        column.setType(dbColumn.getTypeName());
        column.setPk(dbColumn.isPk());
        column.setIncrement(dbColumn.isAutoIncrement());
        column.setNullable(dbColumn.isNullable());
        column.setRequired(!dbColumn.isNullable());
        column.setSize(dbColumn.getSize());
        column.setDigit(dbColumn.getDigit());

        final String unknown = "unknown";
        TypeService bean = Solon.context().getBean(TypeService.class);
        MapDTO maps = bean.loadMap();

        // java
        column.setJavaName(StringUtils.toCamelCase(columnName, false));
        String javaType = maps.getColumnTypeMap().get(dbColumn.getTypeName().toLowerCase());
        column.setJavaType(javaType == null ? unknown : javaType);

        // html
        String htmlType = maps.getHtmlTypeMap().get(dbColumn.getTypeName().toLowerCase());
        column.setHtmlType(htmlType == null ? unknown : htmlType);

        Set<String> ignoreInsertSet = new HashSet<>(StrUtil.split(sets.getIgnoreInsertColumn(), ","));
        Set<String> ignoreSelectSet = new HashSet<>(StrUtil.split(sets.getIgnoreSelectColumn(), ","));
        // 插入
        column.setCanInsert(true);
        // 编辑
        column.setCanUpdate(!dbColumn.isPk() || ignoreInsertSet.contains(columnName));
        // 列表
        column.setCanList(!dbColumn.isPk());
        // 查询
        column.setCanSelect(!dbColumn.isPk() || ignoreSelectSet.contains(columnName));
        // 查询类型
        column.setSelectType("eq");
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
        List<FileDTO> tempFileDtoList = new ArrayList<>();
        tempFiles.forEach(file -> tempFileDtoList.add(FileConvert.to(file)));

        // 注入freemarker 参数
        root.put("table", table);
        root.put("columns", table.getColumns());
        List<Column> pkColumns = table.getColumns().stream().filter(Column::getPk).collect(Collectors.toList());
        // 表无主键情况
        root.put("pkColumn", pkColumns.isEmpty() ? null : pkColumns.get(0));
        root.put("pkColumns", pkColumns);
        root.put("sets", table.getSets());
        root.put(CoreConstant.DTO_KEY, tempFileDtoList);

        // 获取参数
        String argsPath = rootPath + SystemUtils.SEP + SystemUtils.ARGS;
        if (FileUtil.exist(argsPath)) {
            String json = FileUtil.readUtf8String(argsPath);
            @SuppressWarnings("unchecked")
            Map<String, Object> args = JsonUtils.toObject(json, Map.class);
            root.put("args", args);
            Object type = args.get("type");
            if (type != null) {
                String string = type.toString();
                Map<String, TypeAdapter> adapterMap = Solon.context().getBeansMapOfType(TypeAdapter.class);
                Arrays.stream(string.split(",")).forEach(i ->
                        root.put(i, adapterMap.get(CoreConstant.TYPE_ADAPTER_PREFIX + i).getMap(table))
                );
            }
        }
        return root;
    }

    public static String buildOutPath(FileDTO temp, Table table, String code, Environment env) {
        String defOutPath = FreemarkerUtils.getStringVal(env, CoreConstant.OUT_PATH);
        if (StringUtils.isNotBlank(defOutPath)) {
            return defOutPath;
        }

        String suffix = FileUtil.getSuffix(temp.getName());
        String outPath = "";

        RootDirProp rootDir = new RootDirProp();

        if (StrUtil.equalsIgnoreCase(suffix, "java")) {
            List<String> lines = new ArrayList<>();
            IoUtil.readLines(new StringReader(code), lines);
            List<String> packageLines = lines.stream().filter(e -> e.contains("package ")).collect(Collectors.toList());
            List<String> classLines = lines.stream().filter(e -> e.contains("public ")).collect(Collectors.toList());
            String packageLine = packageLines.get(0);
            String classLine = classLines.get(0);
            packageLine = packageLine.replace("package", "").replace(";", "").replace(" ", "").replace(".", "/");
            classLine = StrUtil.split(classLine, " ").get(2);
            outPath = StrUtil.format("/{}/{}/{}.java", rootDir.getJava(), packageLine, classLine);
        } else if (StrUtil.equalsIgnoreCase(suffix, "js")) {
            outPath = StrUtil.format("/{}/{}/{}.js", rootDir.getJs(), table.getModuleName(), table.getBusinessName());
        } else if (StrUtil.equalsIgnoreCase(suffix, "xml") && StrUtil.contains(temp.getName(), "mapper")) {
            outPath = StrUtil.format("/{}/{}Mapper.xml", rootDir.getMapper(), table.getClassName());
        } else if (StrUtil.equalsIgnoreCase(suffix, "sql")) {
            outPath = StrUtil.format("/{}/{}.sql", rootDir.getSql(), table.getClassName());
        } else if (StrUtil.equalsIgnoreCase(suffix, "vue")) {
            outPath = StrUtil.format("/{}/{}/{}/index.vue", rootDir.getVue(), table.getModuleName(), table.getBusinessName());
        } else if (StrUtil.equalsIgnoreCase(suffix, "html")) {
            outPath = StrUtil.format("/{}/{}/{}.html", rootDir.getHtml(), table.getModuleName(), table.getBusinessName());
        } else {
            outPath = StrUtil.format("/default/{}/{}.{}", table.getModuleName(), table.getBusinessName(), suffix);
        }
        return outPath;
    }


    public static String getLast(String s, String splitChar) {
        List<String> split = StrUtil.split(s, splitChar);
        return (StrUtil.isNotBlank(s) ? split.get(split.size() - 1) : "").toLowerCase();
    }

    /**
     * 表名转换成Java类名
     */
    public static String convertClassName(String tableName, String tablePrefix) {
        if (StrUtil.isNotEmpty(tablePrefix)) {
            List<String> searchList = StrUtil.split(tablePrefix, ",");
            tableName = replaceFirst(tableName, searchList);
        }
        return StringUtils.toCamelCase(tableName, true);
    }

    /**
     * 批量替换前缀
     */
    public static String replaceFirst(String rp, List<String> searchList) {
        String text = rp;
        for (String searchString : searchList) {
            if (StrUtil.startWith(text, searchString, true)) {
                text = StrUtil.replaceFirst(text, searchString, "", true);
                break;
            }
        }
        return text;
    }

}

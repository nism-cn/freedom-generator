package org.nism.fg.base.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.Table;
import com.fasterxml.jackson.core.type.TypeReference;
import freemarker.core.Environment;
import org.nism.fg.base.adapter.TypeAdapter;
import org.nism.fg.base.config.props.RootDirProp;
import org.nism.fg.base.constant.CoreConstant;
import org.nism.fg.domain.convert.FileConvert;
import org.nism.fg.domain.dto.FileDTO;
import org.nism.fg.domain.dto.MapDTO;
import org.nism.fg.domain.entity.FgProjectSetting;
import org.nism.fg.domain.entity.FgTable;
import org.nism.fg.domain.entity.FgTableColumn;
import org.nism.fg.service.FgTypeService;
import org.springframework.util.Assert;

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
public class GeneratorUtils {

    private GeneratorUtils() {
    }

    /**
     * 初始化表信息
     */
    public static FgTable buildTable(Table dbTable, FgProjectSetting setting) {
        FgTable table = new FgTable();

        table.setProjectId(setting.getProjectId());

        table.setName(dbTable.getTableName());
        table.setComment(dbTable.getComment());

        table.setClassName(convertClassName(dbTable.getTableName(), setting.getIgnoreTablePrefix()));
        table.setRootPackage(setting.getRootPackage());
        table.setModuleName(getLast(setting.getRootPackage(), "."));
//        table.setBusinessName(getLast(dbTable.getTableName(), "_"));
        table.setBusinessName(table.getClassName());
        table.setAuthor(setting.getAuthor());

        return table;
    }

    /**
     * 初始化列属性字段
     */
    public static FgTableColumn buildColumn(Column dbColumn, FgProjectSetting setting) {
        String columnName = dbColumn.getName();

        // 忽略生成的字段
        Set<String> ignoreColumnSet = new HashSet<>(StrUtil.split(setting.getIgnoreColumn(), ","));
        if (ignoreColumnSet.contains(columnName)) {
            return null;
        }

        FgTableColumn column = new FgTableColumn();
        column.setProjectId(setting.getProjectId());

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
        FgTypeService bean = SpringUtils.getBean(FgTypeService.class);
        MapDTO maps = bean.loadMap();

        // java
        column.setJavaName(StringUtils.toCamelCase(columnName, false));
        String javaType = maps.getColumnTypeMap().get(dbColumn.getTypeName().toLowerCase());
        column.setJavaType(javaType == null ? unknown : javaType);

        // html
        String htmlType = maps.getHtmlTypeMap().get(dbColumn.getTypeName().toLowerCase());
        column.setHtmlType(htmlType == null ? unknown : htmlType);

        Set<String> ignoreInsertSet = new HashSet<>(StrUtil.split(setting.getIgnoreInsertColumn(), ","));
        Set<String> ignoreSelectSet = new HashSet<>(StrUtil.split(setting.getIgnoreSelectColumn(), ","));
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

    public static Map<String, Object> buildTemplateData(FgTable table) {
        FgProjectSetting setting = table.getSetting();
        Assert.notNull(setting, "未找到项目配置信息,请先配置项目!");
        final String rootPath = SystemUtils.getTemplateDir() + SystemUtils.SEP + setting.getTempPath();
        Map<String, Object> root = new HashMap<>();

        // 获取模板
        List<File> tempFiles = FileUtil.loopFiles(rootPath, path -> path.getName().toLowerCase().endsWith(".ftl"));
        Assert.notEmpty(tempFiles, "未找到模板!");
        List<FileDTO> tempFileDtoList = tempFiles.stream().map(FileConvert::to).collect(Collectors.toList());

        // 注入freemarker 参数
        root.put("table", table);
        root.put("columns", table.getColumns());
        List<FgTableColumn> pkColumns = table.getColumns().stream().filter(FgTableColumn::getPk).collect(Collectors.toList());
        // 表无主键情况
        root.put("pkColumn", pkColumns.isEmpty() ? null : pkColumns.get(0));
        root.put("pkColumns", pkColumns);
        root.put("setting", table.getSetting());
        root.put(CoreConstant.DTO_KEY, tempFileDtoList);

        // 获取参数
        String argsPath = rootPath + SystemUtils.SEP + SystemUtils.ARGS;
        if (FileUtil.exist(argsPath)) {
            String json = FileUtil.readUtf8String(argsPath);
            Map<String, Object> args = JsonUtils.toObject(json, new TypeReference<Map<String, Object>>() {
            });
            root.put("args", args);
            Object type = args.get("type");
            if (type != null) {
                String string = type.toString();
                Map<String, TypeAdapter> adapterMap = SpringUtils.getBeansOfType(TypeAdapter.class);
                Arrays.stream(string.split(",")).forEach(i ->
                        root.put(i, adapterMap.get(CoreConstant.TYPE_ADAPTER_PREFIX + i).getMap(table))
                );
            }
        }
        return root;
    }

    public static String buildOutPath(FileDTO temp, FgTable table, String code, Environment env) {
        String defOutPath = FreemarkerUtils.getStringVal(env, CoreConstant.OUT_PATH);
        if (StringUtils.isNotBlank(defOutPath)) {
            return defOutPath;
        }

        String suffix = FileUtil.getSuffix(temp.getName());
        RootDirProp rootDir = SpringUtils.getBean(RootDirProp.class);

        String outPath = "";
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

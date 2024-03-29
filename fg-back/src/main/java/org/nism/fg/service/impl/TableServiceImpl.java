package org.nism.fg.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.nism.fg.base.core.BaseConstant;
import org.nism.fg.base.core.mvc.domain.BaseEntity;
import org.nism.fg.base.core.mvc.service.ServiceImpl;
import org.nism.fg.base.utils.*;
import org.nism.fg.domain.convert.DictConvert;
import org.nism.fg.domain.dto.DictDTO;
import org.nism.fg.domain.dto.FileDTO;
import org.nism.fg.domain.dto.PreviewDTO;
import org.nism.fg.domain.entity.Column;
import org.nism.fg.domain.entity.Config;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.domain.entity.Table;
import org.nism.fg.mapper.ColumnMapper;
import org.nism.fg.mapper.ConfigMapper;
import org.nism.fg.mapper.SetsMapper;
import org.nism.fg.mapper.TableMapper;
import org.nism.fg.service.TableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 自由代码表业务实现层
 *
 * @author nism
 */
@AllArgsConstructor
@Service
public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements TableService {

    private final Configuration cfg;
    private final Configuration stringCfg;
    private final SetsMapper setsMapper;
    private final ColumnMapper columnMapper;
    private final ConfigMapper configMapper;

    @Override
    public Table getById(Serializable id) {
        Table table = baseMapper.selectById(id);
        List<Column> columns = columnMapper.selectList(
                Wrappers.lambdaQuery(Column.class).eq(Column::getTableId, id)
        );
        table.setColumns(columns);
//        this.tableSetDictList(table);
        return table;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Table table, List<Column> columns) {
        baseMapper.updateById(table);
        columnMapper.updateBatch(columns);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        baseMapper.deleteById(id);
        columnMapper.delete(Wrappers.lambdaQuery(Column.class).eq(Column::getTableId, id));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> ids) {
        baseMapper.deleteBatchIds(ids);
        columnMapper.delete(Wrappers.lambdaQuery(Column.class).in(Column::getTableId, ids));
        return true;
    }

    @Override
    public List<Table> selectBatchIdsUnion(Collection<?> ids) {
        List<Table> tables = baseMapper.selectList(Wrappers.lambdaQuery(Table.class).in(Table::getId, ids));
        Assert.notEmpty(tables, "未找到表数据");
        List<Column> columns = columnMapper.selectList(Wrappers.lambdaQuery(Column.class)
                .in(Column::getTableId, tables.stream().map(BaseEntity::getId).collect(Collectors.toSet()))
        );
        Assert.notEmpty(columns, "未找到字段数据");
        List<Sets> setsList = setsMapper.selectList(Wrappers.lambdaQuery(Sets.class)
                .in(Sets::getProjectId, tables.stream().map(Table::getProjectId).collect(Collectors.toSet()))
        );
        Assert.notEmpty(setsList, "未找到设置数据");

        Map<Long, List<Column>> columnMap = columns.stream().collect(Collectors.groupingBy(Column::getTableId));
        Map<Long, Sets> setsMap = setsList.stream().collect(Collectors.toMap(Sets::getProjectId, Function.identity()));

        for (Table table : tables) {
            List<Column> cs = columnMap.get(table.getId());
            Assert.notEmpty(cs, table.getName() + "未找到对应字段数据");
            table.setColumns(cs);
            Sets sets = setsMap.get(table.getProjectId());
            Assert.notNull(sets, table.getName() + "未找到项目配置信息,请先配置项目!");
            table.setSets(sets);
        }
        return tables;
    }

    @Override
    public Table selectByIdUnion(Serializable id) {
        Table table = baseMapper.selectById(id);
        Assert.notNull(table, "未找到表数据");
        List<Column> columns = columnMapper.selectList(Wrappers.lambdaQuery(Column.class).eq(Column::getTableId, id));
        Assert.notEmpty(columns, "未找到字段数据");
        Sets sets = setsMapper.selectOne(Wrappers.lambdaQuery(Sets.class).eq(Sets::getProjectId, table.getProjectId()));
        Assert.notNull(sets, "未找到设置数据");
        table.setColumns(columns);
        table.setSets(sets);
        return table;
    }

    @Override
    public List<PreviewDTO> preview(Serializable id) throws Exception {
        List<PreviewDTO> data = new ArrayList<>();
        Table table = this.selectByIdUnion(id);
        Map<String, Object> root = GenUtils.buildTemplateData(table);
        @SuppressWarnings({"unchecked"})
        List<FileDTO> tempFileDtoList = (List<FileDTO>) root.get(BaseConstant.DTO_KEY);
        root.remove(BaseConstant.DTO_KEY);

        // 根据模板进行渲染
        for (FileDTO fileDTO : tempFileDtoList) {
            if (!StrUtil.equals(fileDTO.getSuffix(), "ftl")) {
                continue;
            }
            TmpDTO tmpDTO = buildCode(fileDTO.getRelativePath(), root);

            PreviewDTO preview = new PreviewDTO();
            preview.setId(fileDTO.getRelativePath());
            preview.setShowName(fileDTO.getName());
            String showLanguage = PreviewDTO.LANGUAGE_MAP.get(FileUtil.getSuffix(fileDTO.getName()));
            preview.setShowLanguage(Optional.ofNullable(showLanguage).orElse("text"));
            preview.setCode(tmpDTO.getCode());
            preview.setPath(buildOutPath(tmpDTO.getEnv(), fileDTO.getName(), root));
            preview.setShowIndex(FreemarkerUtils.getNumberVal(tmpDTO.getEnv(), BaseConstant.SHOW_INDEX).intValue());

            data.add(preview);
        }
        return data.stream().sorted(Comparator.comparing(PreviewDTO::getShowIndex)).collect(Collectors.toList());
    }


    @Override
    public byte[] generator(Collection<?> ids) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (Table table : this.selectBatchIdsUnion(ids)) {
            Map<String, Object> root = GenUtils.buildTemplateData(table);
            @SuppressWarnings({"unchecked"})
            List<FileDTO> tempFileDtoList = (List<FileDTO>) root.get(BaseConstant.DTO_KEY);
            root.remove(BaseConstant.DTO_KEY);
            // 获取模板列表
            for (FileDTO fileDTO : tempFileDtoList) {
                if (!StrUtil.equals(fileDTO.getSuffix(), "ftl")) {
                    continue;
                }
                // 渲染模板
                TmpDTO tmpDTO = buildCode(fileDTO.getRelativePath(), root);
                try {
                    // 添加到zip
                    zip.putNextEntry(new ZipEntry(buildOutPath(tmpDTO.getEnv(), fileDTO.getName(), root)));
                    IoUtil.write(zip, StandardCharsets.UTF_8, false, tmpDTO.getCode());

                    zip.flush();
                    zip.closeEntry();
                } catch (IOException e) {
                    log.error("渲染模板失败，表名：" + table.getName(), e);
                    throw new IllegalArgumentException(e);
                }
            }
        }
        IoUtil.close(zip);
        return outputStream.toByteArray();
    }

    @Override
    public List<cn.hutool.db.meta.Table> getDbTables(Serializable setsId) {
        Assert.isTrue(!"null".equals(setsId), "未找到项目配置信息,请先配置项目");

        Sets sets = setsMapper.selectById(setsId);
        Assert.notNull(sets, "未找到项目配置信息,请先配置项目!");
        List<Table> tableList = baseMapper.selectList(Wrappers.lambdaQuery(Table.class).eq(Table::getProjectId, sets.getProjectId()));

        // 已经生成的表
        List<String> hasGenNames = tableList.stream().map(Table::getName).collect(Collectors.toList());
        List<cn.hutool.db.meta.Table> tables = MetaUtil.getTableList(DataSourceUtils.getDb(sets.getDbInfoId().toString()));

        return tables.stream().filter(e -> !hasGenNames.contains(e.getTableName())).collect(Collectors.toList());
    }

    @Override
    public List<DictDTO> dictList(Serializable tableId) {
        Table table = baseMapper.selectById(tableId);
        Assert.notNull(table, "未找到表信息!");
        Sets sets = setsMapper.selectOne(Wrappers.lambdaQuery(Sets.class).eq(Sets::getProjectId, table.getProjectId()));
        Assert.notNull(sets, "未找到项目配置信息,请先配置项目!");
        return sets.getDictUse() ? getDictData(sets.getDbInfoId(), sets.getDictSql()) : new ArrayList<>();
    }

    private List<DictDTO> getDictData(Long dbInfoId, String sql) {
        List<DictDTO> dictList = new ArrayList<>();
        try {
            List<Entity> dictEntities = Db.use(DataSourceUtils.getDb(dbInfoId.toString())).query(sql);
            for (Entity e : dictEntities) {
                DictDTO dto = DictConvert.to(e);
                if (dto != null) {
                    dictList.add(dto);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("字典表sql异常: " + e.getMessage(), e);
        }
        return dictList;
    }

    /**
     * 开始打码
     *
     * @param codePath 源码路径
     * @param root     传入的变量
     */
    private TmpDTO buildCode(String codePath, Map<String, Object> root) throws TemplateException, IOException {
        // 渲染模板
        Writer sw = new StringWriter();
        Template template = cfg.getTemplate(codePath);
        Environment env = template.createProcessingEnvironment(root, sw);
        env.process();
        String code = sw.toString();
        IoUtil.close(sw);
        TmpDTO dto = new TmpDTO();
        dto.setCode(code);
        dto.setEnv(env);
        return dto;
    }

    /**
     * 根据配置信息生成输出路径
     *
     * @param name 文件名称
     * @param root 渲染所需数据
     * @return 输出路径
     */
    private String buildOutPath(Environment env, String name, Map<String, Object> root) {
        // 输出的文件名称
        String outSuffix = FileUtil.getSuffix(name);
        String outPrefix = FileUtil.getPrefix(name);
        String defaultName = outPrefix + "_" + RandomUtil.randomStringUpper(6) + "." + outSuffix;
        // val 倒序
        String defOutPath = FreemarkerUtils.getStringVal(env, BaseConstant.OUT_PATH);
        if (StringUtils.isNotBlank(defOutPath)) {
            return defOutPath;
        }
        List<Config> pathList = configMapper.selectList(Wrappers.lambdaQuery(Config.class).eq(Config::getType, "OUT_PATH"))
                .stream().sorted(Comparator.comparing(i -> -i.getVal().length())).collect(Collectors.toList());
        String reg = "";
        for (Config c : pathList) {
            if (ReUtil.contains(c.getKey(), name)) {
                reg = c.getVal();
                break;
            }
        }
        StringWriter writer = new StringWriter();
        try {
            Template template = new Template("temporary", reg, stringCfg);
            template.process(root, writer);
        } catch (IOException | TemplateException e) {
            log.error("{}", e);
            return defaultName;
        }
        String path = writer.toString();
        return StrUtil.isEmpty(path) ? defaultName : path;
    }

    /**
     * 简易封装传输对象
     */
    @Data
    private static class TmpDTO {
        private String code;
        private Environment env;
    }

}

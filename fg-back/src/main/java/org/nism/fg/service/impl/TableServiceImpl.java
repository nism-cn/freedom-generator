package org.nism.fg.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nism.fg.base.core.ServiceImpl;
import freemarker.core.Environment;
import freemarker.template.*;
import lombok.AllArgsConstructor;
import org.nism.fg.base.core.CoreConstant;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.base.utils.GenUtils;
import org.nism.fg.base.utils.MetaUtil;
import org.nism.fg.domain.convert.DictConvert;
import org.nism.fg.domain.dto.DictDTO;
import org.nism.fg.domain.dto.FileDTO;
import org.nism.fg.domain.dto.PreviewDTO;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.domain.entity.Table;
import org.nism.fg.domain.entity.Column;
import org.nism.fg.mapper.SetsMapper;
import org.nism.fg.mapper.ColumnMapper;
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
    private final ColumnMapper tableColumnMapper;
    private final SetsMapper projectSettingMapper;

    @Override
    public Table getById(Serializable id) {
        Table table = baseMapper.selectById(id);
        List<Column> columns = tableColumnMapper.selectList(
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
        tableColumnMapper.updateBatch(columns);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        baseMapper.deleteById(id);
        tableColumnMapper.delete(Wrappers.lambdaQuery(Column.class).eq(Column::getTableId, id));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> ids) {
        baseMapper.deleteBatchIds(ids);
        tableColumnMapper.delete(Wrappers.lambdaQuery(Column.class).in(Column::getTableId, ids));
        return true;
    }

    @Override
    public List<Table> selectBatchIdsUnion(Collection<?> ids) {
        List<Table> tables = baseMapper.selectList(Wrappers.lambdaQuery(Table.class).in(Table::getId, ids));
        Assert.notEmpty(tables, "未找到表数据");
        List<Column> columns = tableColumnMapper.selectList(Wrappers.lambdaQuery(Column.class)
                .in(Column::getTableId, tables.stream().map(BaseEntity::getId).collect(Collectors.toSet()))
        );
        Assert.notEmpty(columns, "未找到字段数据");
        List<Sets> settings = projectSettingMapper.selectList(Wrappers.lambdaQuery(Sets.class)
                .in(Sets::getProjectId, tables.stream().map(Table::getProjectId).collect(Collectors.toSet()))
        );
        Assert.notEmpty(settings, "未找到设置数据");

        Map<Long, List<Column>> columnMap = columns.stream().collect(Collectors.groupingBy(Column::getTableId));
        Map<Long, Sets> settingMap = settings.stream().collect(Collectors.toMap(Sets::getProjectId, Function.identity()));

        for (Table table : tables) {
            List<Column> cs = columnMap.get(table.getId());
            Assert.notEmpty(cs, table.getName() + "未找到对应字段数据");
            table.setColumns(cs);
            Sets setting = settingMap.get(table.getProjectId());
            Assert.notNull(setting, table.getName() + "未找到项目配置信息,请先配置项目!");
            table.setSets(setting);
        }
        return tables;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<PreviewDTO> preview(Serializable id) throws Exception {
        List<PreviewDTO> data = new ArrayList<>();
        Table table = this.selectBatchIdsUnion(Collections.singletonList(id)).get(0);
        Map<String, Object> root = GenUtils.buildTemplateData(this.selectBatchIdsUnion(Collections.singletonList(id)).get(0));
        List<FileDTO> tempFileDtoList = (List<FileDTO>) root.get(CoreConstant.DTO_KEY);
        root.remove(CoreConstant.DTO_KEY);

        // 根据模板进行渲染
        for (FileDTO temp : tempFileDtoList) {
            if (!StrUtil.equals(temp.getSuffix(), "ftl")) {
                continue;
            }
            Writer sw = new StringWriter();

            PreviewDTO preview = new PreviewDTO();
            preview.setId(temp.getRelativePath());
            preview.setShowName(temp.getName());
            String showLanguage = PreviewDTO.LANGUAGE_MAP.get(FileUtil.getSuffix(temp.getName()));
            preview.setShowLanguage(showLanguage == null ? "text" : showLanguage);

            Template template = cfg.getTemplate(temp.getRelativePath());
            Environment env = template.createProcessingEnvironment(root, sw);
            env.process();
            String code = sw.toString();
            String outPath = GenUtils.buildOutPath(temp, table, code, env);
            preview.setCode(code);
            preview.setPath(outPath);

            preview.setShowIndex(getShowIndex(env));

            data.add(preview);
            IoUtil.close(sw);
        }
        return data.stream().sorted(Comparator.comparing(PreviewDTO::getShowIndex)).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public byte[] generator(Collection<?> ids) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (Table table : this.selectBatchIdsUnion(ids)) {
            Map<String, Object> root = GenUtils.buildTemplateData(table);
            List<FileDTO> tempFileDtoList = (List<FileDTO>) root.get(CoreConstant.DTO_KEY);
            root.remove(CoreConstant.DTO_KEY);
            // 获取模板列表
            for (FileDTO temp : tempFileDtoList) {
                if (!StrUtil.equals(temp.getSuffix(), "ftl")) {
                    continue;
                }
                // 渲染模板
                Writer sw = new StringWriter();
                Template template = cfg.getTemplate(temp.getRelativePath());
                Environment env = template.createProcessingEnvironment(root, sw);
                env.process();
                String code = sw.toString();
                IoUtil.close(sw);
                try {
                    String outPath = GenUtils.buildOutPath(temp, table, code, env);
                    // 添加到zip
                    zip.putNextEntry(new ZipEntry(outPath));
                    IoUtil.write(zip, StandardCharsets.UTF_8, false, code);

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
    public List<cn.hutool.db.meta.Table> getDbTables(Serializable settingId) {
        Assert.isTrue(!"null".equals(settingId), "未找到项目配置信息,请先配置项目");

        Sets setting = projectSettingMapper.selectById(settingId);

        Assert.notNull(setting, "未找到项目配置信息,请先配置项目!");
        Long dbInfoId = setting.getDbInfoId();

        List<Table> tableList = baseMapper.selectList(Wrappers.lambdaQuery(Table.class).eq(Table::getProjectId, setting.getProjectId()));
        // 已经生成的表
        List<String> hasGenNames = tableList.stream().map(Table::getName).collect(Collectors.toList());
        List<cn.hutool.db.meta.Table> tables = MetaUtil.getTableList(DataSourceUtils.getDb(dbInfoId.toString()));

        return tables.stream().filter(e -> !hasGenNames.contains(e.getTableName())).collect(Collectors.toList());
    }

    @Override
    public List<DictDTO> dictList(Serializable tableId) {
        Table table = baseMapper.selectById(tableId);
        Assert.notNull(table, "未找到表信息!");
        Sets setting = projectSettingMapper.selectOne(Wrappers.lambdaQuery(Sets.class).eq(Sets::getProjectId, table.getProjectId()));
        Assert.notNull(setting, "未找到项目配置信息,请先配置项目!");
        List<DictDTO> dictList = new ArrayList<>();
        if (setting.getDictUse()) {
            try {
                List<Entity> dictEntities = Db.use(DataSourceUtils.getDb(setting.getDbInfoId().toString())).query(setting.getDictSql());
                for (Entity e : dictEntities) {
                    DictDTO dto = DictConvert.to(e);
                    if (dto != null) {
                        dictList.add(dto);
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("字典表sql异常: " + e.getMessage(), e);
            }
        }
        return dictList;
    }

    private Integer getShowIndex(Environment env) {
        try {
            TemplateModel defShowIndex = env.getVariable(CoreConstant.SHOW_INDEX);
            if (null != defShowIndex) {
                if (defShowIndex instanceof SimpleNumber) {
                    return ((SimpleNumber) defShowIndex).getAsNumber().intValue();
                } else {
                    throw new IllegalArgumentException("显示排序参数不正确");
                }
            }
        } catch (TemplateModelException e) {
            throw new IllegalArgumentException("显示排序参数不正确", e);
        }
        return 0;
    }

}

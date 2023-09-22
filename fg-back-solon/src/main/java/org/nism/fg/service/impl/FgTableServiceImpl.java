package org.nism.fg.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.meta.Table;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.solon.service.impl.ServiceImpl;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.ibatis.solon.annotation.Db;
import org.nism.fg.base.constant.CoreConstant;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.utils.Assert;
import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.base.utils.GeneratorUtils;
import org.nism.fg.base.utils.MetaUtil;
import org.nism.fg.domain.convert.DictConvert;
import org.nism.fg.domain.dto.DictDTO;
import org.nism.fg.domain.dto.FileDTO;
import org.nism.fg.domain.dto.PreviewDTO;
import org.nism.fg.domain.entity.FgProjectSetting;
import org.nism.fg.domain.entity.FgTable;
import org.nism.fg.domain.entity.FgTableColumn;
import org.nism.fg.mapper.FgProjectSettingMapper;
import org.nism.fg.mapper.FgTableColumnMapper;
import org.nism.fg.mapper.FgTableMapper;
import org.nism.fg.service.FgTableService;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;

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
@Component
public class FgTableServiceImpl extends ServiceImpl<FgTableMapper, FgTable> implements FgTableService {

    @Inject
    private Configuration cfg;
    @Db
    private FgTableColumnMapper tableColumnMapper;
    @Db
    private FgProjectSettingMapper projectSettingMapper;

    @Override
    public FgTable getById(Serializable id) {
        FgTable table = baseMapper.selectById(id);
        List<FgTableColumn> columns = tableColumnMapper.selectList(
                Wrappers.lambdaQuery(FgTableColumn.class).eq(FgTableColumn::getTableId, id)
        );
        table.setColumns(columns);
//        this.tableSetDictList(table);
        return table;
    }

    //    @Tran
    @Override
    public void modify(FgTable table, List<FgTableColumn> columns) {
        baseMapper.updateById(table);
        tableColumnMapper.updateBatch(columns);
    }

    @Tran
    @Override
    public boolean removeById(Serializable id) {
        baseMapper.deleteById(id);
        tableColumnMapper.delete(Wrappers.lambdaQuery(FgTableColumn.class).eq(FgTableColumn::getTableId, id));
        return true;
    }

    @Tran
    @Override
    public boolean removeByIds(Collection<?> ids) {
        ids.forEach(e -> {
            System.out.println(baseMapper.selectById(e.toString()));
        });
        baseMapper.deleteBatchIds(ids);
        tableColumnMapper.delete(Wrappers.lambdaQuery(FgTableColumn.class).in(FgTableColumn::getTableId, ids));
        return true;
    }

    @Override
    public List<FgTable> selectBatchIdsUnion(Collection<?> ids) {
        List<FgTable> tables = baseMapper.selectList(Wrappers.lambdaQuery(FgTable.class).in(FgTable::getId, ids));
        Assert.notEmpty(tables, "未找到表数据");
        List<FgTableColumn> columns = tableColumnMapper.selectList(Wrappers.lambdaQuery(FgTableColumn.class)
                .in(FgTableColumn::getTableId, tables.stream().map(BaseEntity::getId).collect(Collectors.toSet()))
        );
        Assert.notEmpty(columns, "未找到字段数据");
        List<FgProjectSetting> settings = projectSettingMapper.selectList(Wrappers.lambdaQuery(FgProjectSetting.class)
                .in(FgProjectSetting::getProjectId, tables.stream().map(FgTable::getProjectId).collect(Collectors.toSet()))
        );
        Assert.notEmpty(settings, "未找到设置数据");

        Map<Long, List<FgTableColumn>> columnMap = columns.stream().collect(Collectors.groupingBy(FgTableColumn::getTableId));
        Map<Long, FgProjectSetting> settingMap = settings.stream().collect(Collectors.toMap(FgProjectSetting::getProjectId, Function.identity()));

        for (FgTable table : tables) {
            List<FgTableColumn> cs = columnMap.get(table.getId());
            Assert.notEmpty(cs, table.getName() + "未找到对应字段数据");
            table.setColumns(cs);
            FgProjectSetting setting = settingMap.get(table.getProjectId());
            Assert.notNull(setting, table.getName() + "未找到项目配置信息,请先配置项目!");
            table.setSetting(setting);
        }
        return tables;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<PreviewDTO> preview(Long id) throws Exception {
        List<PreviewDTO> data = new ArrayList<>();
        FgTable table = this.selectBatchIdsUnion(Collections.singletonList(id)).get(0);
        Map<String, Object> root = GeneratorUtils.buildTemplateData(this.selectBatchIdsUnion(Collections.singletonList(id)).get(0));
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
            String outPath = GeneratorUtils.buildOutPath(temp, table, code, env);
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
        for (FgTable table : this.selectBatchIdsUnion(ids)) {
            Map<String, Object> root = GeneratorUtils.buildTemplateData(table);
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
                    String outPath = GeneratorUtils.buildOutPath(temp, table, code, env);
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
    public List<Table> getDbTables(Long settingId) {
        Assert.isTrue(!"null".equals(settingId), "未找到项目配置信息,请先配置项目");

        FgProjectSetting setting = projectSettingMapper.selectById(settingId);

        Assert.notNull(setting, "未找到项目配置信息,请先配置项目!");
        Long dbInfoId = setting.getDbInfoId();

        List<FgTable> tableList = baseMapper.selectList(Wrappers.lambdaQuery(FgTable.class).eq(FgTable::getProjectId, setting.getProjectId()));
        // 已经生成的表
        List<String> hasGenNames = tableList.stream().map(FgTable::getName).collect(Collectors.toList());
        List<Table> tables = MetaUtil.getTableList(DataSourceUtils.getDb(dbInfoId.toString()));

        return tables.stream().filter(e -> !hasGenNames.contains(e.getTableName())).collect(Collectors.toList());
    }

    @Override
    public List<DictDTO> dictList(Long tableId) {
        FgTable table = baseMapper.selectById(tableId);
        Assert.notNull(table, "未找到表信息!");
        FgProjectSetting setting = projectSettingMapper.selectOne(Wrappers.lambdaQuery(FgProjectSetting.class).eq(FgProjectSetting::getProjectId, table.getProjectId()));
        Assert.notNull(setting, "未找到项目配置信息,请先配置项目!");
        List<DictDTO> dictList = new ArrayList<>();
        if (setting.getDictUse()) {
            try {
                List<Entity> dictEntities = cn.hutool.db.Db.use(DataSourceUtils.getDb(setting.getDbInfoId().toString())).query(setting.getDictSql());
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

package org.nism.fg.service.impl;

import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.base.utils.GeneratorUtils;
import org.nism.fg.domain.entity.FgProject;
import org.nism.fg.domain.entity.FgProjectSetting;
import org.nism.fg.domain.entity.FgTable;
import org.nism.fg.domain.entity.FgTableColumn;
import org.nism.fg.mapper.FgProjectMapper;
import org.nism.fg.mapper.FgProjectSettingMapper;
import org.nism.fg.mapper.FgTableColumnMapper;
import org.nism.fg.mapper.FgTableMapper;
import org.nism.fg.service.FgProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自由代码项目业务实现层
 *
 * @author nism
 */
@AllArgsConstructor
@Service
public class FgProjectServiceImpl extends ServiceImpl<FgProjectMapper, FgProject> implements FgProjectService {

    private final FgTableMapper fgTableMapper;
    private final FgTableColumnMapper fgTableColumnMapper;
    private final FgProjectSettingMapper fgProjectSettingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        // 删除table
        fgTableMapper.delete(Wrappers.lambdaQuery(FgTable.class).eq(FgTable::getProjectId, id));
        // 删除字段信息
        fgTableColumnMapper.delete(Wrappers.lambdaQuery(FgTableColumn.class).eq(FgTableColumn::getProjectId, id));
        // 删除设置
        fgProjectSettingMapper.delete(Wrappers.lambdaQuery(FgProjectSetting.class).eq(FgProjectSetting::getProjectId, id));
        // 删除项目
        baseMapper.deleteById(id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTables(Serializable projectId, List<String> tableNames) {
        try {
            FgProjectSetting setting = fgProjectSettingMapper.selectById(projectId);
            Assert.notNull(setting, "未找到项目配置信息,请先配置项目!");
            DataSource dbBean = DataSourceUtils.getDb(setting.getDbInfoId().toString());

            List<FgTable> tableList = new ArrayList<>(tableNames.size());
            List<FgTableColumn> columnList = new ArrayList<>();
            tableNames.forEach(tableName -> {
                Table t = MetaUtil.getTableMeta(dbBean, tableName);
                Long id = IdWorker.getId(t);
                FgTable table = GeneratorUtils.buildTable(t, setting);
                table.setId(id);
                tableList.add(table);
                t.getColumns().forEach(c -> {
                    FgTableColumn column = GeneratorUtils.buildColumn(c, setting);
                    if (column != null) {
                        column.setTableId(id);
                        columnList.add(column);
                    }
                });
            });

            fgTableMapper.insertBatch(tableList);
            fgTableColumnMapper.insertBatch(columnList);
        } catch (Exception e) {
            throw new IllegalArgumentException("导入失败: " + e.getMessage());
        }
    }
}

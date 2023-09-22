package org.nism.fg.service.impl;

import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.solon.service.impl.ServiceImpl;
import org.apache.ibatis.solon.annotation.Db;
import org.nism.fg.base.utils.Assert;
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
import org.noear.solon.annotation.Component;
import org.noear.solon.data.annotation.Tran;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自由代码项目业务实现层
 *
 * @author nism
 */
@Component
public class FgProjectServiceImpl extends ServiceImpl<FgProjectMapper, FgProject> implements FgProjectService {

    @Db
    private FgTableMapper fgTableMapper;
    @Db
    private FgTableColumnMapper fgTableColumnMapper;
    @Db
    private FgProjectSettingMapper fgProjectSettingMapper;

    @Tran
    @Override
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

    @Tran
    @Override
    public void importTables(Long projectId, List<String> tableNames) {
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

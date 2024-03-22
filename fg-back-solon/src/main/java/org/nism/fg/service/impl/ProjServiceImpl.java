package org.nism.fg.service.impl;

import cn.hutool.db.meta.MetaUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.solon.annotation.Db;
import org.nism.fg.base.core.ServiceImpl;
import org.nism.fg.base.utils.Assert;
import org.nism.fg.base.utils.DataSourceUtils;
import org.nism.fg.base.utils.GenUtils;
import org.nism.fg.domain.entity.Proj;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.domain.entity.Table;
import org.nism.fg.domain.entity.Column;
import org.nism.fg.mapper.ProjMapper;
import org.nism.fg.mapper.SetsMapper;
import org.nism.fg.mapper.ColumnMapper;
import org.nism.fg.mapper.TableMapper;
import org.nism.fg.service.ProjService;
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
public class ProjServiceImpl extends ServiceImpl<ProjMapper, Proj> implements ProjService {

    @Db
    private TableMapper tableMapper;
    @Db
    private ColumnMapper columnMapper;
    @Db
    private SetsMapper setsMapper;

    @Tran
    @Override
    public boolean removeById(Serializable id) {
        // 删除table
        tableMapper.delete(Wrappers.lambdaQuery(Table.class).eq(Table::getProjectId, id));
        // 删除字段信息
        columnMapper.delete(Wrappers.lambdaQuery(Column.class).eq(Column::getProjectId, id));
        // 删除设置
        setsMapper.delete(Wrappers.lambdaQuery(Sets.class).eq(Sets::getProjectId, id));
        // 删除项目
        baseMapper.deleteById(id);
        return true;
    }

    @Tran
    @Override
    public void importTables(Long projectId, List<String> tableNames) {
        try {
            Sets setting = setsMapper.selectById(projectId);
            Assert.notNull(setting, "未找到项目配置信息,请先配置项目!");
            DataSource dbBean = DataSourceUtils.getDb(setting.getDbInfoId().toString());

            List<Table> tableList = new ArrayList<>(tableNames.size());
            List<Column> columnList = new ArrayList<>();
            tableNames.forEach(tableName -> {
                cn.hutool.db.meta.Table t = MetaUtil.getTableMeta(dbBean, tableName);
                Long id = IdWorker.getId(t);
                Table table = GenUtils.buildTable(t, setting);
                table.setId(id);
                tableList.add(table);
                t.getColumns().forEach(c -> {
                    Column column = GenUtils.buildColumn(c, setting);
                    if (column != null) {
                        column.setTableId(id);
                        columnList.add(column);
                    }
                });
            });

            tableMapper.insertBatch(tableList);
            columnMapper.insertBatch(columnList);
        } catch (Exception e) {
            throw new IllegalArgumentException("导入失败: " + e.getMessage());
        }
    }
}

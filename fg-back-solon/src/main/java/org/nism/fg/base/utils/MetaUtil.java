package org.nism.fg.base.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.DbUtil;
import cn.hutool.db.meta.Table;
import cn.hutool.db.meta.TableType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MetaUtil extends cn.hutool.db.meta.MetaUtil {

    /**
     * 获得所有表名
     *
     * @param ds 数据源
     * @return 表名列表
     */
    public static List<Table> getTableList(DataSource ds) {
        return getTableList(ds, TableType.TABLE);
    }

    /**
     * 获得所有表名
     *
     * @param ds    数据源
     * @param types 表类型
     * @return 表名列表
     */
    public static List<Table> getTableList(DataSource ds, TableType... types) {
        return getTableList(ds, null, null, types);
    }

    /**
     * 获得所有表名
     *
     * @param ds        数据源
     * @param schema    表数据库名，对于Oracle为用户名
     * @param tableName 表名
     * @param types     表类型
     * @return 表名列表
     * @since 3.3.1
     */
    public static List<Table> getTableList(DataSource ds, String schema, String tableName, TableType... types) {
        final List<Table> tables = new ArrayList<>();
        Connection conn = null;
        try {
            conn = ds.getConnection();

            // catalog和schema获取失败默认使用null代替
            final String catalog = getCatalog(conn);
            if (null == schema) {
                schema = getSchema(conn);
            }

            final DatabaseMetaData metaData = conn.getMetaData();
            try (final ResultSet rs = metaData.getTables(catalog, schema, tableName, Convert.toStrArray(types))) {

                if (null != rs) {
                    String table, comment;
                    while (rs.next()) {
                        table = rs.getString("TABLE_NAME");
                        comment = rs.getString("REMARKS");
                        if (StrUtil.isNotBlank(table)) {
                            Table t = new Table(table);
                            t.setComment(comment);
                            tables.add(t);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new DbRuntimeException("Get tables error!", e);
        } finally {
            DbUtil.close(conn);
        }
        return tables;
    }

}

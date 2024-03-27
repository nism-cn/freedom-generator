package org.nism.fg.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.domain.dto.DictDTO;

import java.util.List;

/**
 * 生成表对象
 *
 * @author nism
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("F_TABLE")
public class Table extends BaseEntity {

    /*** 表名 */
    private Long projectId;
    /*** 表名 */
    private String name;
    /*** 表描述 */
    private String comment;

    /*** 小驼峰 tableName */
    private String camel;
    /*** 大驼峰 TableName */
    private String upCamel;
    /*** 小下划线 table_name */
    private String underline;
    /*** 大下划线 TABLE_NAME */
    private String upUnderline;
    /*** 模块名 */
    private String module;
    /*** 字段 */
    private transient List<Column> columns;
    /*** 设置 */
    private transient Sets sets;
    /*** 字典 */
    private transient List<DictDTO> dictList;
}
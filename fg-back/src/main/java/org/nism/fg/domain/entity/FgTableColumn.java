package org.nism.fg.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nism.fg.base.core.BaseEntity;

/**
 * 生成表字段对象
 *
 * @author nism
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fg_table_column")
public class FgTableColumn extends BaseEntity {

    /*** 项目主键 */
    private Long projectId;
    /*** 表主键 */
    private Long tableId;
    /* ========================= 数据库原有属性 ========================= */
    /*** 字段名 */
    private String name;
    /*** 字段描述 */
    private String comment;
    /*** 字段类型 */
    private String type;
    /*** 主键 */
    private Boolean pk;
    /*** 自增 */
    private Boolean increment;
    /*** 空 */
    private Boolean nullable;
    /*** 必填 */
    private Boolean required;
    /*** 大小 */
    private Long size;
    /*** 数据长度 */
    private Integer digit;
    /*** 排序 */
    private Integer sort;
    /* ========================= 需要定义属性 ========================= */
    /*** 插入 */
    private Boolean canInsert;
    /*** 更新 */
    private Boolean canUpdate;
    /*** 列表 */
    private Boolean canList;
    /*** 查询 */
    private Boolean canSelect;
    /*** 查询方式 */
    private String selectType;

    /* ========================= 自动生成属性 ========================= */
    /*** java字段名 */
    private String javaName;
    /*** java类型 */
    private String javaType;
    /*** html类型 */
    private String htmlType;
    /*** 字典类型 */
    private String dictType;


}
package org.nism.fg.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.JacksonSerializer;

import java.util.Map;

/**
 * 生成表字段对象
 *
 * @author nism
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "F_COLUMN" ,autoResultMap = true)
public class Column extends BaseEntity {

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

    /* ========================= 自动生成属性 ========================= */
    /*** 小驼峰 columnName */
    private String camel;
    /*** 大驼峰 ColumnName */
    private String upCamel;
    /*** 小下划线 column_name */
    private String underline;
    /*** 大下划线 COLUMN_NAME */
    private String upUnderline;
    /*** 类型的映射 */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> types;
    /*** 字典类型 */
    private String dictType;


}
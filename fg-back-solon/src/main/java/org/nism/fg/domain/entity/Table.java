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

    /*** Java类名 */
    private String className;
    /*** Java包 */
    private String rootPackage;
    /*** 模块名 */
    private String moduleName;
    /*** 业务名 */
    private String businessName;
    /*** 作者 */
    private String author;
    /*** 字段 */
    private transient List<Column> columns;
    /*** 设置 */
    private transient Sets sets;
    /*** 字典 */
    private transient List<DictDTO> dictList;
}
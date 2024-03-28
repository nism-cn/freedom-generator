package org.nism.fg.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nism.fg.base.core.mvc.domain.BaseEntity;

/**
 * 项目设置对象
 *
 * @author nism
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("F_SETS")
public class Sets extends BaseEntity {

    /*** 关联项目 */
    private Long projectId;
    /*** 关联数据源 */
    private Long dbInfoId;
    /*** 关联模板 */
    private String tempPath;
    /*** Java包 */
    private String pkg;
    /*** 作者 */
    private String author;
    /*** 是否开启字典表查询 */
    private Boolean dictUse;
    /*** 字典表查询sql */
    private String dictSql;

}

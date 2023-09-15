package org.nism.fg.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nism.fg.base.core.BaseEntity;

/**
 * 项目设置对象
 *
 * @author nism
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("FG_PROJECT_SETTING")
public class FgProjectSetting extends BaseEntity {

    /*** 关联项目 */
    private Long projectId;
    /*** 关联数据源 */
    private Long dbInfoId;
    /*** 关联模板 */
    private String tempPath;
    /*** Java包 */
    private String rootPackage;
    /*** 作者 */
    private String author;
    /*** 忽略生成表前缀 */
    private String ignoreTablePrefix;
    /*** 忽略生成表字段 (继承了父类) */
    private String ignoreColumn;
    /*** 忽略生成表插入字段 (主键,创建时间等) */
    private String ignoreInsertColumn;
    /*** 忽略生成表显示字段 (修改页主键显示等) */
    private String ignoreSelectColumn;
    /*** 是否开启字典表查询 */
    private Boolean dictUse;
    /*** 字典表查询sql */
    private String dictSql;

}

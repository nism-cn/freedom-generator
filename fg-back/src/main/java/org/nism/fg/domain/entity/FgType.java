package org.nism.fg.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nism.fg.base.core.BaseEntity;

/**
 * 类型表对象
 *
 * @author nism
 * @since 1.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("FG_TYPE")
public class FgType extends BaseEntity {

    /*** 类型 */
    private String mold;
    /*** 表名 */
    private String name;
    /*** 表描述 */
    private String val;
    /*** 禁用 */
    private Boolean dis;
    /*** 描述 */
    private String remark;
}
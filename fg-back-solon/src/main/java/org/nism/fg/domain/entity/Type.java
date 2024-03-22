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
public class Type extends BaseEntity {

    /*** 类型 */
//    @NotBlank(message = "类型不能为空")
    private String mold;
    /*** 描述 */
//    @NotBlank(message = "描述不能为空")
    private String name;
    /*** 值 */
//    @NotBlank(message = "类型值不能为空")
    private String val;
    /*** 禁用 */
    private Boolean dis;
    /*** 描述 */
    private String remark;
}
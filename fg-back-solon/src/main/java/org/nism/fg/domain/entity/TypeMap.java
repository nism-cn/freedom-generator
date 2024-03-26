package org.nism.fg.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nism.fg.base.core.BaseEntity;

/**
 * 类型映射表对象
 *
 * @author nism
 * @since 1.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("F_TYPE_MAP")
public class TypeMap extends BaseEntity {

    /*** 类型 */
    private String typeMold;
    /*** 类型主键 */
//    @NotNull(message = "类型不能为空")
    private Long typeId;
    /*** 类型值 */
    private transient String typeVal;
    /*** 映射类型 */
    private String mapMold;
    /*** 映射类型主键 */
//    @NotNull(message = "映射类型不能为空")
    private Long mapId;
    /*** 映射类型值 */
    private transient String mapVal;
}
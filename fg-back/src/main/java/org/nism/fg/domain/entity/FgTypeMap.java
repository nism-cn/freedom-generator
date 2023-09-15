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
@TableName("FG_TYPE_MAP")
public class FgTypeMap extends BaseEntity {

    /*** 表名 */
    private String typeMold;
    /*** 表名 */
    private Long typeId;
    /*** 表名 */
    private transient String typeVal;
    /*** */
    private String mapMold;
    /*** 表名 */
    private Long mapId;
    /*** 表名 */
    private transient String mapVal;
}
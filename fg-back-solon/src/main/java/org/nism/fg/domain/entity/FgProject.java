package org.nism.fg.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.Tabs;
import org.nism.fg.domain.enums.TabsType;

/**
 * 项目对象
 *
 * @author nism
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("FG_PROJECT")
public class FgProject extends BaseEntity implements Tabs {

    private String name;
    @TableField(exist = false)
    private String mainType = TabsType.project.name();

}

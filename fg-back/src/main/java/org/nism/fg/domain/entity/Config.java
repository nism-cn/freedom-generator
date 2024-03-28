package org.nism.fg.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nism.fg.base.core.mvc.domain.BaseEntity;

/**
 * 基本参数
 *
 * @author nism
 * @since 2.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("F_CONFIG")
public class Config extends BaseEntity {

    @TableField("`type`")
    private String type;
    private String name;
    @TableField("`key`")
    private String key;
    private String val;
    private String remark;
}
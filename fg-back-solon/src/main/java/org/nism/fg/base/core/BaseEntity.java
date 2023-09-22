package org.nism.fg.base.core;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;


/**
 * 实体基类
 *
 * @author inism
 * @since 1.0.0
 */
@Data
public class BaseEntity implements Serializable {

    @TableId
    private Long id;
    @TableField(exist = false)
    private String search;

}

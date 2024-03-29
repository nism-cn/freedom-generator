package org.nism.fg.base.core.mvc.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/***
 * Entity基类
 *
 *
 */
@Data
public class Strapi implements Serializable {

    @TableField(exist = false)
    private List<String> fields;

    @TableField(exist = false)
    private List<String> sorts;

    @TableField(exist = false)
    private Map<String, Map<String, Object>> filters;


}

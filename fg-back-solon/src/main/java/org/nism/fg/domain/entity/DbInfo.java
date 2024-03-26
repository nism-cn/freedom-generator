package org.nism.fg.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nism.fg.base.core.BaseEntity;

/**
 * 数据源对象
 *
 * @author nism
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("F_DB_INFO")
public class DbInfo extends BaseEntity {

    private String name;
    private String jdbcUrl;
    private String username;
    private String password;
    private String remark;

}

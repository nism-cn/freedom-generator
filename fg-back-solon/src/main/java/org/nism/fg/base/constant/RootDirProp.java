package org.nism.fg.base.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * 字段类型映射
 *
 * @author inism
 * @since 1.0.0
 */
@Getter
@Setter
public class RootDirProp {

    private String sql = "sql";
    private String java = "main/java";
    private String mapper = "main/resources/mapper";
    private String js = "ui/api";
    private String vue = "ui/views";
    private String html = "ui/html";

}

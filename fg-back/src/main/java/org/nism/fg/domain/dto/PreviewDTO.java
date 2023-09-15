package org.nism.fg.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码预览对象
 *
 * @author nism
 * @since 1.0.0
 */
@Data
public class PreviewDTO implements Serializable {

    public static final Map<String, String> LANGUAGE_MAP = new HashMap<>();

    static {
        LANGUAGE_MAP.put("java", "java");
        LANGUAGE_MAP.put("xml", "xml");
        LANGUAGE_MAP.put("vue", "html");
        LANGUAGE_MAP.put("js", "javascript");
        LANGUAGE_MAP.put("ftl", "freemarker2");
        LANGUAGE_MAP.put("sql", "sql");
        LANGUAGE_MAP.put("html", "html");
    }


    /*** 预览键 方便前端tabs处理 */
    private String id;
    /*** 预览名称 */
    private String showName;
    /*** 预览语言 */
    private String showLanguage;
    /*** 预览代码(内容) */
    private String code;
    /*** 预览生成路径 */
    private String path;

}

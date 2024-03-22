package org.nism.fg.base.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 常量池
 *
 * @author inism
 * @since 1.0.0
 */
public interface CoreConstant {

    Map<String, ClassLoader> JDBC_CLASS_LOADER_MAP = new ConcurrentHashMap<>();

    String ENV_DEV = "dev";
    String DTO_KEY = "DTO_KEY";
    String DB_BEAN_KEY = "ds";
    String OUT_PATH = "_out_path_";
    String SHOW_INDEX = "_show_index_";

    String TYPE_DB = "DB";
    String TYPE_JAVA = "JAVA";
    String TYPE_HTML = "HTML";
    String TYPE_SQL = "SQL";

}

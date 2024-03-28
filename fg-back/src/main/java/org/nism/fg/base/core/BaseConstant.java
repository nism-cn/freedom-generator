package org.nism.fg.base.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 常量池
 *
 * @author inism
 * @since 1.0.0
 */
public interface BaseConstant {

    Map<String, ClassLoader> JDBC_CLASS_LOADER_MAP = new ConcurrentHashMap<>();

    String ENV_DEV = "dev";
    String DTO_KEY = "DTO_KEY";
    String DB_BEAN_KEY = "ds";
    String OUT_PATH = "_out_path_";
    String SHOW_INDEX = "_show_index_";

    String TYPE_ADAPTER_PREFIX = "type-adapter-";

}

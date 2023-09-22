package org.nism.fg.base.utils;

/**
 * 系统工具类
 *
 * @author nism
 * @since 1.0.0
 */
public class SystemUtils {

    private SystemUtils() {
    }

    public static final String HOME = "fg.home";
    public static final String SEP = "/";
    public static final String DATA_DIR = "data";
    public static final String TEMPLATE_DIR = "template";
    public static final String LOGS_DIR = "logs";
    public static final String LIBS_DIR = "libs";
    public static final String ARGS = "args.json";

    /**
     * 获取当前工作目录
     */
    public static String getUserDir() {
        return (System.getProperty("user.dir") + SEP + HOME).replace("\\", "/");
    }

    /**
     * 获取模版目录
     */
    public static String getTemplateDir() {
        return (getUserDir() + SEP + TEMPLATE_DIR).replace("\\", "/");
    }

    /**
     * 获取数据目录
     */
    public static String getDataDir() {
        return (getUserDir() + SEP + DATA_DIR).replace("\\", "/");
    }

    /**
     *
     */
    public static String getLogsDir() {
        return (getUserDir() + SEP + LOGS_DIR).replace("\\", "/");
    }

    /**
     *
     */
    public static String getLibsDir() {
        return (getUserDir() + SEP + LIBS_DIR).replace("\\", "/");
    }

}

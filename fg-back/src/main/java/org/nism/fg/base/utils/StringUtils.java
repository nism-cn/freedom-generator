package org.nism.fg.base.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类
 *
 * @author inism
 * @since 1.0.0
 */
public class StringUtils extends StrUtil {

    private StringUtils() {
    }

    /**
     * 驼峰命名
     *
     * @param s    转换
     * @param flag true 大驼峰/ false 小驼峰
     * @return 大驼峰 HelloWord 小驼峰 helloWord
     */
    public static String toCamelCase(String s, boolean flag) {
        s = toCamelCase(s.toLowerCase());
        return flag ? upperFirst(s) : lowerFirst(s);
    }

}

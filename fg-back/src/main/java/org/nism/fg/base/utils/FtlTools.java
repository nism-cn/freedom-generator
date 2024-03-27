package org.nism.fg.base.utils;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FtlTools {

    private static final String SPLIT = ",";

    private FtlTools() {
    }

    /**
     * 包含数据
     */
    public static boolean contains(String text, String search) {
        return contains(text, search, SPLIT);
    }

    public static boolean contains(String text, String search, String split) {
        String s = Optional.ofNullable(text).orElse("");
        Collection<String> list = Arrays.asList(s.split(split));
        return contains(list, search);
    }

    public static boolean contains(Collection<String> textList, String search) {
        for (String text : textList) {
            if (text.equals(search)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 批量替换前缀
     */
    public static String removeFirst(String search, String rp) {
        return removeFirst(search, rp, SPLIT);
    }

    public static String removeFirst(String search, String rp, String split) {
        String s = Optional.ofNullable(search).orElse("");
        Collection<String> list = Arrays.asList(s.split(split));
        return removeFirst(list, rp);
    }

    public static String removeFirst(Collection<String> searchList, String rp) {
        String text = rp;
        for (String searchString : searchList) {
            if (StrUtil.startWith(text, searchString, true)) {
                text = StrUtil.replaceFirst(text, searchString, "", true);
                break;
            }
        }
        return text;
    }

    /**
     * 获取最后部分
     */
    public static String getLast(String s, String splitChar) {
        List<String> split = StrUtil.split(s, splitChar);
        return (StrUtil.isNotBlank(s) ? split.get(split.size() - 1) : "").toLowerCase();
    }

}

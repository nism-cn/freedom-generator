package org.nism.fg.base.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class FtlTools {

    private FtlTools() {
    }


    public static boolean contains(String text, String search) {
        return contains(text, search, ",");
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

}

package org.nism.fg.base.utils;

import org.noear.snack.ONode;

public class JsonUtils {

    public static String writeValueAsString(Object o) {
        return ONode.stringify(o);
    }

    public static <T> T readValue(String source, Class<T> clz) {
        return ONode.deserialize(source, clz);
    }
}

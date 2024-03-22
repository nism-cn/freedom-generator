package org.nism.fg.base.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.SneakyThrows;

public class JsonUtils {

    private static ObjectMapper OBJECT_MAPPER;

    private JsonUtils() {
    }

    @SneakyThrows
    public static String toString(Object o) {
        return getObjectMapper().writeValueAsString(o);
    }

    @SneakyThrows
    public static String toString(Object o, String[] fields) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(fields);
        FilterProvider filters = new SimpleFilterProvider().addFilter("ExcludeFilters", filter);
        return getObjectMapper().writer(filters).writeValueAsString(o);
    }

    @SneakyThrows
    public static <T> T toObject(String s, Class<T> clazz) {
        return getObjectMapper().readValue(s, clazz);
    }

    @SneakyThrows
    public static <T> T toObject(String s, TypeReference<T> type) {
        return getObjectMapper().readValue(s, type);
    }

    private static ObjectMapper getObjectMapper() {
        return null == OBJECT_MAPPER ? new ObjectMapper() : OBJECT_MAPPER;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        OBJECT_MAPPER = objectMapper;
    }
}

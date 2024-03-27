package org.nism.fg.base.core;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 *
 */
public class JacksonSerializer extends JsonSerializer<String> {

    private static ObjectMapper OBJECT_MAPPER;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(StrUtil.isEmpty(s) ? null : getObjectMapper().readValue(s, Object.class));
    }

    private static ObjectMapper getObjectMapper() {
        return null == OBJECT_MAPPER ? new ObjectMapper() : OBJECT_MAPPER;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        OBJECT_MAPPER = objectMapper;
    }

}
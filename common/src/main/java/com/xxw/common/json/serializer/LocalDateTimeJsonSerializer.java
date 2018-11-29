package com.xxw.common.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xxw
 * @date 2018/9/10
 */
public class LocalDateTimeJsonSerializer extends JsonSerializer implements ContextualSerializer {

    private String pattern;

    private LocalDateTimeJsonSerializer() {}

    private LocalDateTimeJsonSerializer(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if (StringUtils.isNotBlank(pattern)) {
            jsonGenerator.writeObject(DateTimeFormatter.ofPattern(pattern).format((LocalDateTime) o));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
            throws JsonMappingException {
        LocalDateTimeFormat localDateTimeFormat = beanProperty.getAnnotation(LocalDateTimeFormat.class);
        return new LocalDateTimeJsonSerializer(localDateTimeFormat.pattern());
    }
}

package com.xxw.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author xxw
 * @date 2018/8/27
 */
@Component
public class JacksonUtils implements ApplicationContextAware {

    private static ObjectMapper objectMapper;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        setObjectMapper(applicationContext.getBean(ObjectMapper.class));
    }

    private synchronized static void setObjectMapper(ObjectMapper applicationContextObjectMapper) {
        objectMapper = applicationContextObjectMapper;
    }

    public static String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new JacksonException(e);
        }
    }

    public static <T> T readValue(String content, Class<?> parametrized, Class... parameterClasses) {
        JavaType valueType = objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
        try {
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new JacksonException(e);
        }
    }

    public static JsonNode readTree(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (IOException e) {
            throw new JacksonException(e);
        }
    }

    public static <T> T readValue(JsonNode jsonNode, Class<T> valueType) {
        try {
            return objectMapper.readValue(jsonNode.traverse(), valueType);
        } catch (IOException e) {
            throw new JacksonException(e);
        }
    }

    public static <T> T readValue(JsonNode jsonNode, Class<?> parametrized, Class... parameterClasses) {
        JavaType valueType = objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
        try {
            return objectMapper.readValue(jsonNode.traverse(), valueType);
        } catch (IOException e) {
            throw new JacksonException(e);
        }
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return objectMapper.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object fromValue, Class<?> parametrized, Class<?>... parameterClasses) {
        JavaType valueType = objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
        return objectMapper.convertValue(fromValue, valueType);
    }

    public static String writeValueAsPrettyString(Object value) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }
}

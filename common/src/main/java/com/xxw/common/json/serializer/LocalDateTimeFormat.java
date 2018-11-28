package com.xxw.common.json.serializer;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * @author xxw
 * @date 2018/9/10
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = LocalDateTimeJsonSerializer.class)
public @interface LocalDateTimeFormat {

    String pattern() default "";
}

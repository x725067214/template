package com.xxw.common.web;

import com.xxw.common.exception.ExceptionController;
import com.xxw.common.json.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author xxw
 * @date 2018/9/28
 */
@Component
@Aspect
@Order(1)
public class ValidationAspect {

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void validate(JoinPoint joinPoint) throws Throwable {
        if (joinPoint.getTarget() instanceof ExceptionController) {
            return;
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        for (int i = 0; i < parameters.length; ++i) {
            Parameter parameter = parameters[i];
            Object arg = joinPoint.getArgs()[i];

            boolean handleEmptyBody = parameter.isAnnotationPresent(RequestBody.class) && arg == null;
            if (handleEmptyBody) {
                if (parameter.getType().isAssignableFrom(List.class)) {
                    Type[] types = ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments();
                    joinPoint.getArgs()[i] = JacksonUtils.readValue("[]", List.class, types[0].getClass());
                }
                else {
                    joinPoint.getArgs()[i] = parameter.getType().newInstance();
                }
            }

            boolean handleNullArg = parameter.isAnnotationPresent(NotNull.class) && arg == null;
            if (handleNullArg) {
                throw new IllegalArgumentException(parameterNames[i] + "不能为空");
            }

            boolean handleBlankArg = parameter.isAnnotationPresent(NotBlank.class) && (arg == null
                    || arg instanceof String && StringUtils.isBlank((String) arg));
            if (handleBlankArg) {
                throw new IllegalArgumentException(parameterNames[i] + "不能为空");
            }

            boolean handleEmptyArg = parameter.isAnnotationPresent(NotEmpty.class) && (arg == null
                    || arg instanceof Object[] && ((Object[]) arg).length == 0);
            if (handleEmptyArg) {
                throw new IllegalArgumentException(parameterNames[i] + "不能为空");
            }
        }
    }
}

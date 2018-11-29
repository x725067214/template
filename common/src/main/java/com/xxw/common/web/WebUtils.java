package com.xxw.common.web;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xxw
 * @date 2018/9/30
 */
public class WebUtils {

    private WebUtils() {
        throw new IllegalStateException("Utility Class");
    }

    public static HttpServletRequest getRequest() {
        return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}

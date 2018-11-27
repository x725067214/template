package com.xxw.admin.exception;

/**
 * @author xxw
 * @date 2018/8/8
 */
public class BusinessException extends RuntimeException {

    /**
     * 状态码
     */
    private String code;

    /**
     * 数据
     */
    private Object data;

    private BusinessException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public static BusinessException build(String message) {
        return new BusinessException("fail", message, null);
    }

    public static BusinessException build(String message, Object data) {
        return new BusinessException("fail", message, data);
    }

    public static BusinessException build(String code, String message) {
        return new BusinessException(code, message, null);
    }

    public static BusinessException build(String code, String message, Object data) {
        return new BusinessException(code, message, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

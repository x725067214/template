package com.xxw.admin.model;

import com.xxw.admin.exception.BusinessException;

import static com.xxw.admin.constant.StatusConstants.FAIL;
import static com.xxw.admin.constant.StatusConstants.SUCCESS;

/**
 * @author xxw
 * @date 2018/8/10
 */
public class ResponseResult {

    /**
     * 状态码
     */
    private String code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private Object body;

    private ResponseResult(String code, String msg, Object body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    public static ResponseResult success() {
        return new ResponseResult(SUCCESS, null, null);
    }

    public static ResponseResult success(Object body) {
        return new ResponseResult(SUCCESS, null, body);
    }

    public static ResponseResult fail(String msg) {
        return new ResponseResult(FAIL, msg, null);
    }

    public static ResponseResult fail(String msg, Object body) {
        return new ResponseResult(FAIL, msg, body);
    }

    public static ResponseResult build(String code, String msg) {
        return new ResponseResult(code, msg, null);
    }

    public static ResponseResult build(String code, String msg, Object body) {
        return new ResponseResult(code, msg, body);
    }

    public static ResponseResult build(BusinessException businessException) {
        return new ResponseResult(businessException.getCode(), businessException.getMessage(),
                businessException.getData());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}

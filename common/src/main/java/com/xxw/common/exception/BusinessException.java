package com.xxw.common.exception;

import static com.xxw.common.constant.StatusConstants.FAIL;

/**
 * @author xxw
 * @date 2018/8/8
 */
public class BusinessException extends RuntimeException {

    /**
     * 状态码
     */
    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = FAIL;
    }

    public String getCode() {
        return code;
    }
}

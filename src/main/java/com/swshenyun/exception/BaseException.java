package com.swshenyun.exception;

import com.swshenyun.common.ErrorCode;

/**
 * 业务异常
 */
public class BaseException extends RuntimeException {

    private final int code;

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}

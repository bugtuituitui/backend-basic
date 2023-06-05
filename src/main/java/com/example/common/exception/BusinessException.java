package com.example.common.exception;

/**
 * @author kfg
 * @date 2022/9/16 14:51
 */
public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}

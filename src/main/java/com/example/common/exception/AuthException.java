package com.example.common.exception;

/**
 * @author kfg
 * @date 2022/12/5 18:01
 */
public class AuthException extends BusinessException {
    public AuthException(int code, String message) {
        super(code, message);
    }
}

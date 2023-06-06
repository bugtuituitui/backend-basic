package com.example.common.pay.enums;

/**
 * @author kfg
 * @date 2023/4/26 13:05
 */
public enum PayStatus {

    SUCCESS("success", "支付成功"),
    ERROR("error", "支付失败");

    private String code;

    private String msg;

    PayStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String msg() {
        return msg;
    }
}

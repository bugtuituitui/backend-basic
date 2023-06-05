package com.example.common.lang;

/**
 * 响应状态枚举
 *
 * @author kfg
 * @date 2022/5/5 15:44
 */
public enum ResultEnum {

    SUCCESS(200, "请求成功"),
    ERROR(-1, "请求失败"),
    NO_AUTH(-2, "无权限"),
    AUTH_ERROR(-3, "认证失败"),

    DATABASE_QUERY_NO_DATA(30001, "查询无数据"),
    DATABASE_INSERT_ERROR(30002, "数据插入错误"),
    DATABASE_UPDATE_ERROR(30003, "数据更新错误"),
    DATABASE_DELETE_ERROR(30004, "数据删除错误");

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}


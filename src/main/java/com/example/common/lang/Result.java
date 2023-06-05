package com.example.common.lang;

import lombok.Data;

/**
 * @author kfg
 * @date 2022/5/5 15:35
 */
@Data
public class Result<T> {

    private Integer code;
    private String message;
    private T data;
    private Long total;

    public static Result success() {
        return success(null);
    }

    public static Result success(Object data) {
        return success(data, 0);
    }

    public static Result success(Object data, long total) {
        Result result = new Result();
        result.setTotal(total);
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static Result fail(String message) {
        Result result = fail();
        result.setMessage(message);
        return result;
    }

    public static Result fail() {
        return fail(ResultEnum.ERROR);
    }

    public static Result fail(ResultEnum resultEnum) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMessage());
        return result;
    }

}

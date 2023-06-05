package com.example.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.exception.BusinessException;
import com.example.common.lang.ResultEnum;

/**
 * 校验异常工具类
 *
 * @author kfg
 * @date 2023/6/4 15:54
 */
public class AssertUtils {

    // 对象为空
    public static void isEmpty(Object object, String message) {
        if (isEmpty(object)) {
            throwException(message);
        }
    }

    // 对象不为空
    public static void isNotEmpty(Object object, String message) {
        if (!isEmpty(object)) {
            throwException(message);
        }
    }

    // 对象相等
    public static void isEqual(Object a, Object b, String message) {
        if (ObjectUtil.equal(a, b)) {
            throwException(message);
        }
    }

    // 对象不相等
    public static void isNotEqual(Object a, Object b, String message) {
        if (!ObjectUtil.equal(a, b)) {
            throwException(message);
        }
    }

    public static boolean isEmpty(Object object) {
        return ObjectUtil.isEmpty(object);
    }

    public static void throwException(String message) {
        throwException(null, message);
    }

    public static void throwException(ResultEnum resultEnum, String message) {

        if (ObjectUtil.isEmpty(resultEnum)) {
            resultEnum = ResultEnum.ERROR;
        }
        throw new BusinessException(resultEnum.getCode(), message);
    }
}

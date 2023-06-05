package com.example.common.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * AOP 工具类
 *
 * @author kfg
 * @date 2023/6/4 23:25
 */
public class AspectUtils {

    /**
     * 获取方法上的注解
     *
     * @param point
     * @param clazz
     * @return
     */
    public static Annotation getAnnotation(JoinPoint point, Class clazz) {
        Method method = getRealMethod(point);
        return method.getAnnotation(clazz);
    }

    /**
     * 获取切入点所在代理方法
     *
     * @param point
     * @return
     */
    public static Method getProxyMethod(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        return method;
    }

    /**
     * 获取切入点所在具体方法
     *
     * @param point
     * @return
     */
    public static Method getRealMethod(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Method realMethod = null;
        try {
            realMethod = point.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return realMethod;
    }
}

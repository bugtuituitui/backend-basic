package com.example.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口权限注解
 *
 * @author kfg
 * @date 2023/6/5 0:26
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    /**
     * 权限类型
     * @return
     */
    Type type();

    /**
     * 可选角色
     * @return
     */
    String[] hasRole() default {};

    /**
     * 唯一角色
     * @return
     */
    String onlyRole() default "";

    enum Type {
        HasRole,
        OnlyRole;
    }

}

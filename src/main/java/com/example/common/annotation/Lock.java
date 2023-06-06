package com.example.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 *
 * @author kfg
 * @date 2023/6/6 17:24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

    /**
     * key前缀,默认取方法全限定名，如果在不同方法上对同一个资源做分布式，就自定义
     * @return
     */
    String prefix() default "";

    /**
     * 加锁名称
     * @return
     */
    String key() default "";

    /**
     * 加锁时长
     * @return
     */
    int lockTime() default 30;

    /**
     * 等待时长 默认-1, 加锁失败，直接返回
     * @return
     */
    int waitTime() default -1;

    /**
     * 时间单位
     * @return
     */
    TimeUnit unit() default TimeUnit.SECONDS;
}

package com.example.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 接口频控注解
 *
 * @author kfg
 * @date 2023/5/31 0:39
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FrequencyControl {

    /**
     * 接口名称
     *
     * @return
     */
    String api();

    /**
     * 可调用次数
     *
     * @return
     */
    int count() default 3;

    /**
     * 时长
     *
     * @return
     */
    int time() default 30;

    /**
     * 时间单位
     *
     * @return
     */
    TimeUnit unit() default TimeUnit.SECONDS;

}

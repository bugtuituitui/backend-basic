package com.example.common.aspect;

import com.example.common.annotation.FrequencyControl;
import com.example.common.lang.Result;
import com.example.common.utils.AspectUtils;
import com.example.common.utils.RedisUtils;
import com.example.common.utils.RequestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 接口频控切面
 *
 * @author kfg
 * @date 2023/5/31 0:42
 */
@Aspect
@Component
public class FrequencyControlAspect {

    private static final String MSG = "请求频繁，请稍后再试";

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.example.common.annotation.FrequencyControl)")
    void pointCut() {
    }

    /**
     * 执行切入代码
     *
     * @param point
     * @return
     */
    @Around(value = "pointCut()")
    public Result deal(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = RequestUtils.getRequest();

        // 反射获取切入点方法
        FrequencyControl frequencyControl = AspectUtils.getRealMethod(point).getAnnotation(FrequencyControl.class);

        // 请求主机地址
        String host = RequestUtils.getIP();

        Object cacheCount = redisUtils.get(host);

        // 比较次数
        if (cacheCount != null) {
            int count = (int) cacheCount;
            if (count >= frequencyControl.count()) {
                return Result.fail(MSG);
            }
            redisUtils.set(host, ++count, frequencyControl.time());
        } else {
            redisUtils.set(host, 1, frequencyControl.time());
        }

        return (Result) point.proceed(point.getArgs());
    }
}

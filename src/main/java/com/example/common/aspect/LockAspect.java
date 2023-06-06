package com.example.common.aspect;

import com.example.common.annotation.Lock;
import com.example.common.service.LockService;
import com.example.common.utils.AspectUtils;
import com.example.common.utils.RequestUtils;
import com.example.common.utils.SpElUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式锁切面
 *
 * @author kfg
 * @date 2023/6/6 17:30
 */
@Component
@Aspect
@Order(3) // 在事务之前执行
public class LockAspect {

    @Autowired
    private LockService lockService;

    @Pointcut("@annotation(com.example.common.annotation.Lock)")
    void pointCut() {
    }

    @Around("pointCut()")
    Object execute(ProceedingJoinPoint point) throws Throwable {

        Method method = AspectUtils.getRealMethod(point);
        Lock lock = method.getAnnotation(Lock.class);

        // key 前缀
        String prefix = StringUtils.isEmpty(lock.prefix()) ? SpElUtils.getMethodKey(method) : lock.prefix();
        String key = SpElUtils.parseSpEl(method, point.getArgs(), lock.key());

        System.out.println("===");
        System.out.println(prefix + ":" + key);
        return lockService.executeLockWithException(prefix + ":" + key, lock.waitTime(), lock.unit(), point::proceed);
    }
}

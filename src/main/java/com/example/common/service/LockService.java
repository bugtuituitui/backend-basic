package com.example.common.service;

import com.example.common.exception.BusinessException;
import lombok.SneakyThrows;
import org.apache.poi.ss.formula.functions.T;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;



/**
 * 分布式锁服务类
 *
 * @author kfg
 * @date 2023/6/6 17:06
 */
@Component
public class LockService {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 加锁操作
     *
     * @param time
     * @param unit
     * @param key
     * @param invoker 业务执行函数
     * @return
     */
    public <T> T executeLockWithException(String key, int time, TimeUnit unit, Invoker<T> invoker) throws Throwable {

        RLock rLock = redissonClient.getLock(key);

        if (!rLock.tryLock(time, unit)) {
            throw new BusinessException(-1, "服务器正忙，请稍后再试");
        }

        try {
            // 执行业务
            return invoker.get();
        } finally {
            rLock.unlock();
        }

    }

    @SneakyThrows
    public <T> T executeLock(String key, int time, TimeUnit unit, Invoker<T> invoker) {
        return executeLockWithException(key, time, unit, invoker::get);
    }

    /**
     * 函数接口
     *
     * @param <T>
     */
    @FunctionalInterface
    public interface Invoker<T> {
        T get() throws Throwable;
    }
}


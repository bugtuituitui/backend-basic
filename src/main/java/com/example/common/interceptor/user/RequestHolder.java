package com.example.common.interceptor.user;

/**
 * 请求信息保存
 *
 * @author kfg
 * @date 2023/6/4 22:28
 */
public class RequestHolder {

    private static final ThreadLocal<RequestInfo> holder = new ThreadLocal<>();

    public static RequestInfo get() {
        return holder.get();
    }

    public static void put(RequestInfo requestInfo) {
        holder.set(requestInfo);
    }

    public static void clear() {
        holder.remove();
    }
}

package com.example.common.interceptor.user;

import com.example.common.constant.Header;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 请求拦截器 用于获取用户信息
 *
 * @author kfg
 * @date 2023/6/4 22:25
 */
@Order(1)
@Component
public class InfoCollectInterceptor implements HandlerInterceptor {

    /**
     * 收集用户信息
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String UID = Optional.ofNullable(request.getAttribute(Header.TOKEN)).map(Object::toString).orElse(null);
        String IP = request.getRemoteHost();
        RequestHolder.put(new RequestInfo(IP, UID, "1"));

        return true;
    }

    /**
     * 清除用户信息
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.clear();
    }
}

package com.example.common.interceptor.user;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TOKEN 拦截器
 *
 * @author kfg
 * @date 2023/6/4 22:41
 */
@Order(0)
@Component
public class TokenInterceptor implements HandlerInterceptor {


    /**
     * 校验token
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        String token = request.getHeader(Header.TOKEN);
//
//        if (JwtUtils.isValid(token)) {
//            request.setAttribute(TOKEN, token);
//            return true;
//        }
//        return false;

        return true;
    }
}

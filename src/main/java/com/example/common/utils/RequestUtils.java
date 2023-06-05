package com.example.common.utils;

import com.example.common.constant.Header;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求工具类
 *
 * @author kfg
 * @date 2023/6/4 22:50
 */
public class RequestUtils {

    /**
     * 获取请求IP
     *
     * @return
     */
    public static String getIP() {
        return getRequest().getRemoteHost();
    }

    /**
     * 获取请求头的TOKEN
     *
     * @return
     */
    public static String getToken() {
        return getRequest().getHeader(Header.TOKEN);
    }

    /**
     * 获取UID
     *
     * @return
     */
    public static String getUID() {

        String token = getToken();
        if (token == null) {
            return null;
        }

        return JwtUtils.getCredential(token);
    }

    /**
     * 获取HttpServlet
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    /**
     * 获取请求头TOKEN
     *
     * @param request
     * @return
     */
    public static String getToken(ServletRequest request) {
        return ((HttpServletRequest) request).getHeader(Header.TOKEN);
    }

    /**
     * 请求转发
     *
     * @param request
     * @param path
     */
    public static void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}

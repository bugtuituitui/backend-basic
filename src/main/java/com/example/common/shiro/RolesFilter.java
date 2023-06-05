package com.example.common.shiro;

import com.example.common.utils.RequestUtils;
import com.example.common.utils.ResponseUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户鉴权
 *
 * @author kfg
 * @date 2022/12/6 14:15
 */
public class RolesFilter extends RolesAuthorizationFilter {

    // 是否允许访问
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        Subject subject = getSubject(request, response);
        String[] roles = (String[]) mappedValue;

        // 认证
        try {
            executeLogin(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 鉴权
        for (String role : roles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-control-Allow-Origin", req.getHeader("Origin"));
        res.setHeader("Access-control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        res.setHeader("Access-control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(HttpStatus.OK.value());
            // 返回true则继续执行拦截链，返回false则中断后续拦截，直接返回，option请求显然无需继续判断，直接返回
            return false;
        }
        return super.preHandle(request, response);
    }


    // 执行认证
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {

        ShiroToken token = new ShiroToken(RequestUtils.getToken(request));
        // 使用自定义的JWTToken而不是默认的UsernamePasswordToken
        getSubject(request, response).login(token);
        // 调用了realm中的认证方法，没有出现异常则证明认证成功
        return true;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        ResponseUtils.writeText(response, -1, "无权限");

        return false;
    }

}

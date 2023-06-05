package com.example.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义凭证
 *
 * @author kfg
 * @date 2022/12/5 12:01
 */
public class ShiroToken implements AuthenticationToken {

    private String token;

    public ShiroToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}


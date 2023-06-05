package com.example.common.shiro;

import com.example.common.utils.JwtUtils;
import com.example.common.utils.RedisUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自定义Realm，用来实现用户的认证和授权
 * Realm：父类抽象类
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private RedisUtils redisUtils;

    // 让shiro支持我们自定义的token，即如果传入的token时JWTToken则放行
    // 必须重写不然shiro会报错
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ShiroToken;
    }

    // 检验权限时调用
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String token = principalCollection.toString();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // TODO 数据库获取用户角色权限

        // user = service.getUser(JwtUtils.getCredential(token));
        // role = user.getRole();

        // info.add(role);
        // info.addRole("admin");

        ////info.addStringPermissions(user.getPermission());
        return info;
    }

    // 认证和鉴权时调用
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {

        // 该类被重写，实际是返回TOKEN
        String token = (String) authenticationToken.getCredentials();

        // TOKEN是否有效
        if (!JwtUtils.isValid(token)) {
            throw new AuthenticationException();
        }

        // redis是否存在TOKEN
        if (redisUtils.get(token) == null) {
            throw new AuthenticationException();
        }

        return new SimpleAuthenticationInfo(token, token, this.getName());
    }
}


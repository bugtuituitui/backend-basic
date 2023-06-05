package com.example.common.shiro;


import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro 配置
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        // 添加自己的过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        // filterMap.put("authc", new ShiroFilter());
        filterMap.put("roles", new RolesFilter());
        shiroFilterFactoryBean.setFilters(filterMap);


        // 编写过滤规则
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        // 访问 /unauthorized/**时直接放行

        // 登录 注册
        filterRuleMap.put("/api/v1/web/user/login", "anon");
        filterRuleMap.put("/api/v1/web/user/reg", "anon");
        filterRuleMap.put("/api/v1/admin/user/login", "anon");

        // 验证码接口
        filterRuleMap.put("/api/v1/web/imgCode/generate", "anon");
        filterRuleMap.put("/api/v1/admin/imgCode/generate", "anon");

        filterRuleMap.put("/api/v1/web/page/data", "anon");
        filterRuleMap.put("/api/v1/web/blog/list", "anon");
        filterRuleMap.put("/api/v1/web/blog/detail", "anon");
        filterRuleMap.put("/api/v1/web/collect/list", "anon");
        filterRuleMap.put("/api/v1/web/sheet/get", "anon");

        // filterRuleMap.put("/api/v1/web/online", "anon");

        filterRuleMap.put("/api/v1/web/gpt/chat", "anon");

        // filterRuleMap.put("/api/v1/web/item/list", "anon");
        // filterRuleMap.put("/api/v1/web/sale/seckill", "anon");

        // 开放支付宝回调接口
        filterRuleMap.put("/api/v1/web/pay/callback", "anon");

        filterRuleMap.put("/api/v1/web/wares/list", "anon");


        filterRuleMap.put("/weixin/**", "anon");

        // filterRuleMap.put("/**", "authc");

        filterRuleMap.put("/api/v1/admin/**", "roles[ADMIN]");
        filterRuleMap.put("/api/v1/web/**", "roles[ORDINARY_USER]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(shiroRealm);
        // 关闭session
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(defaultSubjectDAO);

        return defaultWebSecurityManager;
    }


    /**
     * 添加注解支持，如果不加的话很有可能注解失效
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {

        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(defaultWebSecurityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}

package com.example.common.interceptor;

import com.example.common.interceptor.user.InfoCollectInterceptor;
import com.example.common.interceptor.user.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor配置
 *
 * @author kfg
 * @date 2023/6/4 23:00
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Autowired
    private InfoCollectInterceptor infoCollectInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**");
        registry.addInterceptor(infoCollectInterceptor)
                .addPathPatterns("/**");
    }
}

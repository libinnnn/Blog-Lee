package com.lee.config;

/**
 * @author libin
 * @date 2020-10-19 17:37
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器注入
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private URLInterceptor urlInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(urlInterceptor).addPathPatterns("/admin/**")
            .excludePathPatterns("/css/**","/js/**","/images/**","/lib/**");
    }
}

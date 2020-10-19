package com.lee.config;

/**
 * @author libin
 * @date 2020-10-19 17:36
 */

import com.lee.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * URL拦截，做对应处理
 *
 */
@Component
public class URLInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(URLInterceptor.class);

    /**
     * 请求前置处理（后置处理同理）
     *
     * @param request
     * @param response
     * @param handler
     * @return boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        if (path.contains("/admin/")) {
            logger.info("拦截 requestUrl: {}", path);
            // 进行前置处理
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            if(user == null){
                response.sendRedirect(request.getContextPath()+"/admin");
            }
            return true;
            // 或者 return false; 禁用某些请求
        } else {
            return true;
        }
    }
}

package com.lee.config;
import com.lee.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * @author libin
 * @date 2020-10-18 22:38
 *
 * 尚未加入认证！！！
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("dffffffffff");
        if (isLoginAttempt(request, response)) {
            log.info("认证中");
            ModelAndView mv = new ModelAndView();
            mv.setViewName("admin/login");
            return true;
        }
        return false;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        User user = (User)req.getSession().getAttribute("user");
        return user != null;
    }
}

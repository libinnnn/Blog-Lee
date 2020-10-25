package com.lee.aspect;

import com.lee.dto.RequestLog;
import com.lee.pojo.SysLog;
import com.lee.pojo.User;
import com.lee.service.LogService;
import com.lee.util.IPUtil;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class LogAspect {

    @Autowired
    LogService logService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //这个注解表示一个切面,execution()表示拦截哪些
    //拦截controller下所有类和所有方法
    @Pointcut("execution(* com.lee.controller.*.*(..))")
    public void log() {

    }


    //横切，在log()之前执行
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        //这个attributes可以获得url和ip
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("Request: {}", requestLog);
    }

    @After("log()")
    public void doAfter() {
        //logger.info("-------------After----------");
    }

    //捕获返回的内容
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result) {
        logger.info("Result: {}" + result);
    }


    // log注解
    //切点，只要是有注解的都拦截
    @Pointcut("@annotation(com.lee.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    //环绕通知
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 获取 request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 设置 IP 地址
        String ip = IPUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 获取用户信息
        User user = new User();
        try{
            if((User)SecurityUtils.getSubject().getPrincipal() != null){ // user对象为空，则不设值
                user = (User)SecurityUtils.getSubject().getPrincipal();
            }
        } catch (Exception e){
            logger.info(e.getMessage());
            user.setUsername("");
        }

        SysLog sysLog = new SysLog();
        sysLog.setIp(ip);
        sysLog.setUsername(user.getUsername());
        sysLog.setTime(time);

        logService.saveLog(point, sysLog);

        return result;
    }
}

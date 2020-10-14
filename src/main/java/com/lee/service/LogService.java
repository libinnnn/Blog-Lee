package com.lee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lee.pojo.SysLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

/**
 * @author libin
 * @date 2020-10-09 22:44
 */
public interface LogService {
    @Async(value = "CommonThreadPool")
    void saveLog(ProceedingJoinPoint point, SysLog log) throws JsonProcessingException;
}

package com.lee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lee.pojo.SysLog;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author libin
 * @date 2020-10-09 22:44
 */
public interface LogService {
    void saveLog(ProceedingJoinPoint point, SysLog log) throws JsonProcessingException;
}

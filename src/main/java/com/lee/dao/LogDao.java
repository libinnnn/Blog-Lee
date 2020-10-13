package com.lee.dao;

import com.lee.pojo.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author libin
 * @date 2020-10-09 23:17
 */
@Repository
@Mapper
public interface LogDao {
    int saveLog(SysLog sysLog);
}

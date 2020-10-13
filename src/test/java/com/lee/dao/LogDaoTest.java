package com.lee.dao;

import com.lee.pojo.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author libin
 * @date 2020-10-10 08:39
 */
@SpringBootTest
@Slf4j
public class LogDaoTest {
    @Autowired
    LogDao logDao;

    @Test
    void saveLog(){
        SysLog sysLog = new SysLog();
        sysLog.setIp("127.0.0.1");
        logDao.saveLog(sysLog);
    }


}

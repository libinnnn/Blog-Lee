package com.lee.runner;

/**
 * @author libin
 * @date 2020-10-11 12:36
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.net.InetAddress;

@Order
@Slf4j
@Component
public class StartedUpRunner implements ApplicationRunner {

    @Autowired
    private ConfigurableApplicationContext context;

    @Value("${server.port}")
    private String port;

    @Override
    public void run(ApplicationArguments args) throws UnknownHostException {


        InetAddress address = InetAddress.getLocalHost();
        String localAddress = "http://" + address.getHostAddress() + ":" + port;
        if (context.isActive()) {
            log.info(" __    ___   _      ___   _     ____ _____  ____ ");
            log.info("Lee Blog启动完毕，时间：" + LocalDateTime.now());
            log.info("URL ：" + localAddress);
            log.info(" __    ___   _      ___   _     ____ _____  ____ ");
        }
    }
}

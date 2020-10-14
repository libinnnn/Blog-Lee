package com.lee.config;

/**
 * @author libin
 * @date 2020-10-14 21:20
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 由于开启了保存日志的异步机制，因此需要配置一个执行的线程池
 */
@Configuration
public class AsyncExecutorPoolConfig extends AsyncConfigurerSupport {

    @Bean(name = "CommonThreadPool")
    public TaskExecutor taskExecutor() {
        //Springboot把JUC中的包，封装了一层使用
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("CommonThreadPool-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
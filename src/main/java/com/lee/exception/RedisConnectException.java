package com.lee.exception;

/**
 * @author libin
 * @date 2020-10-23 12:55
 */


/**
 * Redis 连接异常
 */
public class RedisConnectException extends Exception {

    private static final long serialVersionUID = 1639374111871115063L;

    public RedisConnectException(String message) {
        super(message);
    }
}

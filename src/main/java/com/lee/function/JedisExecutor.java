package com.lee.function;

import com.lee.exception.RedisConnectException;

/**
 * @author libin
 * @date 2020-10-24 22:50
 */
@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}

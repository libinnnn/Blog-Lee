package com.lee.service;

import com.lee.dto.BlogQuery;
import com.lee.exception.RedisConnectException;
import com.lee.pojo.Blog;

import java.util.List;

/**
 * @author libin
 * @date 2020-10-25 20:42
 */
public interface CacheService {
    int updateAllBlog() throws Exception;

    List<BlogQuery> getAllBlog() throws Exception;

    void deleteAllBlog() throws RedisConnectException;

}

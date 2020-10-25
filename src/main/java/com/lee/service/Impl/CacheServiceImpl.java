package com.lee.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.dao.BlogDao;
import com.lee.domain.BlogConstant;
import com.lee.dto.BlogQuery;
import com.lee.exception.RedisConnectException;
import com.lee.pojo.Blog;
import com.lee.service.CacheService;
import com.lee.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author libin
 * @date 2020-10-25 20:44
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private RedisService redisService;

    @Override
    public int updateAllBlog() throws Exception {
        List<BlogQuery> allBlogQuery = blogDao.getAllBlogQuery();
        redisService.set(BlogConstant.BLOG_CACHE_PREFIX,mapper.writeValueAsString(allBlogQuery));
        return 0;
    }

    @Override
    public List<BlogQuery> getAllBlog() throws Exception {
        String BlogQueryList = redisService.get(BlogConstant.BLOG_CACHE_PREFIX);
        if (StringUtils.isBlank(BlogQueryList)){
            return null;
        }else{
            //反序列化
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, BlogQuery.class);
            return this.mapper.readValue(BlogQueryList, type);
        }
    }

    @Override
    public void deleteAllBlog() throws RedisConnectException {
        redisService.del(BlogConstant.BLOG_CACHE_PREFIX);
    }
}

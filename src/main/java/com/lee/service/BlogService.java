package com.lee.service;

import com.lee.dto.*;
import com.lee.exception.RedisConnectException;
import com.lee.pojo.Blog;

import java.util.List;

public interface BlogService {

    ShowBlog getBlogById(Long id);

    List<BlogQuery> getAllBlog() throws Exception;

    int saveBlog(Blog blog) throws Exception;

    int updateBlog(ShowBlog showBlog) throws RedisConnectException;

    int deleteBlog(Long id) throws RedisConnectException;

    List<BlogQuery> getBlogBySearch(SearchBlog searchBlog);

    //修改recommend,因为recommend从前台接收只能接收字符串，但数据库中的Integer类型，所以转一下
    void transformRecommend(SearchBlog searchBlog);

    List<FirstPageBlog> getAllFirstPageBlog() throws Exception;

    List<RecommendBlog> getRecommendedBlog();

    List<FirstPageBlog> getSearchBlog(String query);

    DetailedBlog getDetailedBlog(Long id);

    //根据TypeId获取博客列表，在分类页进行的操作
    List<FirstPageBlog> getByTypeId(Long typeId);

    List<FirstPageBlog> getByTagId(Long tagId);
}

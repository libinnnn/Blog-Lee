package com.lee.service.Impl;

import com.lee.dao.BlogDao;
import com.lee.dto.*;
import com.lee.exception.NotFountException;
import com.lee.exception.RedisConnectException;
import com.lee.pojo.Blog;
import com.lee.pojo.Tag;
import com.lee.service.BlogService;
import com.lee.service.CacheService;
import com.lee.util.MarkdownUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;
    @Autowired
    private CacheService cacheService;

    @Override
    public ShowBlog getBlogById(Long id) {
        return blogDao.getBlogById(id);
    }

    @Override
    public List<BlogQuery> getAllBlog() throws Exception {
        List<BlogQuery> allBlogQuery = cacheService.getAllBlog();
//        log.info("allBlogQuery:{}",allBlogQuery.toString());
        if(allBlogQuery == null){
            cacheService.updateAllBlog();
            allBlogQuery = blogDao.getAllBlogQuery();
            log.info("AllBlog 未命中缓存！！！");
        }
        return allBlogQuery;
    }

    @Override
    public int saveBlog(Blog blog) throws Exception {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        //将标签的数据存到t_blogs_tag表中
        List<Tag> tags = blog.getTags();
        BlogAndTag blogAndTag = null;
        for (Tag tag : tags) {
            blogAndTag = new BlogAndTag(tag.getId(),blog.getId());
            blogDao.saveBlogAndTag(blogAndTag);
        }
        //删除缓存
        cacheService.deleteAllBlog();
        cacheService.deleteAllBlog();
        return blogDao.saveBlog(blog);
    }

    @Override
    public int updateBlog(ShowBlog showBlog) throws RedisConnectException {
        showBlog.setUpdateTime(new Date());
        //删除缓存
        cacheService.deleteAllBlog();
        cacheService.deleteAllBlog();
        return blogDao.updateBlog(showBlog);
    }

    @Override
    public int deleteBlog(Long id) throws RedisConnectException {
        blogDao.deleteBlogAndTag(id);
        blogDao.deleteBlog(id);
        //删除缓存
        cacheService.deleteAllBlog();
        cacheService.deleteAllBlog();
        return 1;
    }

    @Override
    public List<BlogQuery> getBlogBySearch(SearchBlog searchBlog) {
        return blogDao.searchByTitleOrTypeOrRecommend(searchBlog);
    }

    @Override
    public void transformRecommend(SearchBlog searchBlog) {
        if (!"".equals(searchBlog.getRecommend()) && null != searchBlog.getRecommend()) {
            searchBlog.setRecommend2(1);
        }
    }

    @Override
    public List<FirstPageBlog> getAllFirstPageBlog() throws Exception {
        List<FirstPageBlog> firstPageBlogList = cacheService.getAllFirstPageBlog();
        if(firstPageBlogList == null){
            firstPageBlogList = blogDao.getFirstPageBlog();
            cacheService.updateAllFirstPageBlog();
            log.info("AllFirstPageBlog 未命中缓存");
        }
        return blogDao.getFirstPageBlog();
    }


    @Override
    public List<RecommendBlog> getRecommendedBlog() {
        List<RecommendBlog> allRecommendBlog = blogDao.getAllRecommendBlog();
        List<RecommendBlog> allRecommendedBlog = new ArrayList<>();
        for (RecommendBlog recommendBlog : allRecommendBlog) {
            if (recommendBlog.isRecommend() == true) {
                allRecommendedBlog.add(recommendBlog);
            }
        }
        return allRecommendedBlog;
    }

    @Override
    public List<FirstPageBlog> getSearchBlog(String query) {
        return blogDao.getSearchBlog(query);
    }

    @Override
    public DetailedBlog getDetailedBlog(Long id) {
        DetailedBlog detailedBlog = blogDao.getDetailedBlog(id);
        if (detailedBlog == null) {
            throw new NotFountException("该博客不存在");
        }
        String content = detailedBlog.getContent();
        detailedBlog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return detailedBlog;
    }

    @Override
    public List<FirstPageBlog> getByTypeId(Long typeId) {
        return blogDao.getByTypeId(typeId);
    }

    @Override
    public List<FirstPageBlog> getByTagId(Long tagId) {
        return blogDao.getByTagId(tagId);
    }


}

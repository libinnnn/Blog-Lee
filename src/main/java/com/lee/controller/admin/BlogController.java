package com.lee.controller.admin;

import com.lee.annotation.Log;
import com.lee.dto.BlogQuery;
import com.lee.dto.SearchBlog;
import com.lee.dto.ShowBlog;
import com.lee.exception.RedisConnectException;
import com.lee.pojo.Blog;
import com.lee.pojo.Tag;
import com.lee.pojo.Type;
import com.lee.pojo.User;
import com.lee.service.BlogService;
import com.lee.service.TagService;
import com.lee.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/admin")
@Slf4j
public class BlogController {
    @Value("${file-save-path}")
    private String fileSavePath;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    public void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.getAdminType());
        model.addAttribute("tags", tagService.getAdminTag());
    }

    //显示
    @Log("显示")
    @GetMapping("/blogs")
    public String list(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) throws Exception {
        PageHelper.startPage(pageNum, 3);
        List<BlogQuery> allBlog = blogService.getAllBlog();
        PageInfo<BlogQuery> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo", pageInfo);
        setTypeAndTag(model);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userName", user.getUsername());
        return "admin/blogs";
    }


    //删除
    @Log("删除")
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) throws RedisConnectException {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }

    //去新增页面
    @Log("新增博客页面")
    @GetMapping("/blogs/input")
    public String toAdd(Model model) {
        setTypeAndTag(model);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userName", user.getUsername());
        return "admin/blogs-input";
    }

    //新增
    @Log("新增博客")
    @PostMapping("/blogs")
    public String add(Blog blog, RedirectAttributes attributes, HttpSession session) throws Exception {
        blog.setUser((User) session.getAttribute("user"));
        //设置blog的type
        blog.setType(typeService.getType(blog.getType().getId()));
        //设置blog中typeId属性
        blog.setTypeId(blog.getType().getId());
        //给blog中的List<Tag>赋值
        blog.setTags(tagService.getTagByString(blog.getTagIds()));
        //设置用户id
        blog.setUserId(blog.getUser().getId());
        blogService.saveBlog(blog);
        attributes.addFlashAttribute("message", "新增成功");
        return "redirect:/admin/blogs";
    }

    @Log("查找")
    @PostMapping("/blogs/search")
    public String search(SearchBlog searchBlog,Model model,
                       @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        //将recommend转换一下
        blogService.transformRecommend(searchBlog);
        //动态sql可以解决
        List<BlogQuery> blogBySearch = blogService.getBlogBySearch(searchBlog);
        PageHelper.startPage(pageNum, 3);
        PageInfo<BlogQuery> pageInfo = new PageInfo<>(blogBySearch);
        model.addAttribute("pageInfo", pageInfo);
        setTypeAndTag(model);
        model.addAttribute("message", "查询成功");
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userName", user.getUsername());
        return "admin/blogs";
    }

    //将数据回返编辑页面
    @Log("返回编辑页")
    @GetMapping("/blogs/{id}/input")
    public String toUpdate(@PathVariable Long id,Model model) {
        ShowBlog blogById = blogService.getBlogById(id);
        List<Type> allType = typeService.getAdminType();
        List<Tag> allTag = tagService.getAdminTag();
        model.addAttribute("blog", blogById);
        model.addAttribute("types", allType);
        model.addAttribute("tags", allTag);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userName", user.getUsername());
        return "admin/blogs-update";
    }

    @Log("更新")
    @PostMapping("/blogs/update")
    public String editPost(ShowBlog showBlog,RedirectAttributes attributes) throws RedisConnectException {
        blogService.updateBlog(showBlog);
        attributes.addFlashAttribute("message", "修改成功");
        return "redirect:/admin/blogs";
    }

    @Log("上传图片")
    @ResponseBody
    @PostMapping(value = "/blogs/uploadfile")
    public JSONObject hello(HttpServletRequest request,
                            HttpServletResponse response,
                            HttpSession session,
                            @RequestParam(value = "editormd-image-file", required = false) MultipartFile attach) throws Exception {

        JSONObject jsonObject=new JSONObject();
        try{
            String fileName = attach.getOriginalFilename();

            //保存文件操作
            File dir = new File(fileSavePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            log.info("图片上传，保存位置：" + fileSavePath);
            File newFile = new File(fileSavePath +  fileName);
            attach.transferTo(newFile);

            // 用于editor.md的回显
            jsonObject.put("success", 1);
            jsonObject.put("message", "上传成功");
            jsonObject.put("url", "/images/" + fileName);


        }catch (Exception e){
            jsonObject.put("success", 0);
            throw new Exception("上传异常！！！");
        }

        return jsonObject;
    }

}

package com.lee.controller.admin;

import com.lee.annotation.Log;
import com.lee.pojo.Tag;
import com.lee.pojo.User;
import com.lee.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @Log("获取标签")
    @GetMapping("/tags")
    public String list(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum, 4);
        List<Tag> allTag = tagService.getAdminTag();
        PageInfo<Tag> pageInfo = new PageInfo<>(allTag);
        model.addAttribute("pageInfo", pageInfo);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userName", user.getUsername());
        return "admin/tags";
    }

    //去新增页面
    @GetMapping("/tags/input")
    public String toAdd() {
        return "admin/tags-input";
    }

    @Log("新增标签")
    @PostMapping("/tags/add")
    public String add(Tag tag, RedirectAttributes attributes) {
        System.out.println("前端传过来的表单" + tag);
        Tag tag1 = tagService.getByName(tag.getName());
        if (tag1 != null) {
            //不为空说明数据库已有
            attributes.addFlashAttribute("message", "已有这个标签，不能添加重复");
            return "redirect:/admin/tags/input";
        }
        tagService.saveTag(tag);
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id,Model model) {
        model.addAttribute("tag", tagService.getById(id));
        return "admin/tags-update";
    }

    @Log("更新标签")
    @PostMapping("/tags/update")
    public String editPost(Tag tag) {
        tagService.updateTag(tag);
        return "redirect:/admin/tags";
    }

    @Log("删除标签")
    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id) {
        tagService.deleteTag(id);
        return "redirect:/admin/tags";
    }
}

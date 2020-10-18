package com.lee.controller.admin;

import com.lee.annotation.Log;
import com.lee.pojo.Type;
import com.lee.pojo.User;
import com.lee.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    //列表页
    @Log("显示分类")
    @GetMapping("/types")
    public String list(Model model,@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum, 3);
        List<Type> allType = typeService.getAdminType();
        //得到分页结果对象
        PageInfo<Type> pageInfo = new PageInfo<>(allType);
        model.addAttribute("pageInfo", pageInfo);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userName", user.getUsername());
        return "admin/types";
    }

    //去新增页面
    @GetMapping("/types/input")
    public String toAdd() {
        return "admin/types-input";
    }


    //Add
    @Log("新增分类")
    @PostMapping("/types/add")
    public String Add(Type type, RedirectAttributes attributes, BindingResult result) {
        System.out.println("前端传过来的表单" + type);
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null) {
            attributes.addFlashAttribute("message", "不能添加重复的类");
            return "redirect:/admin/types/input";
        }
        //添加操作
        typeService.saveType(type);
        return "redirect:/admin/types";
    }


    //到修改页面
    @Log("修改分类")
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        System.out.println(id);
        Type type = typeService.getType(id);
        System.out.println(type);
        model.addAttribute("type", type);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userName", user.getUsername());
        return "admin/types-update";
    }

    //进行修改
    @PostMapping("/types/update")
    public String editPost(Type type) {
        System.out.println(type);
        typeService.updateType(type);
        return "redirect:/admin/types";
    }


    //删除
    @Log("删除分类")
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }



}

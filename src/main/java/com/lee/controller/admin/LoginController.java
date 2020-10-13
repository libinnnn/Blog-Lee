package com.lee.controller.admin;

import com.lee.annotation.Log;
import com.lee.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String loginPage() {
        return "admin/login";
    }

    @Log("登录")
    @RequestMapping("/login")
    public String login(Model model,
                        @RequestParam String username,
                        @RequestParam String password,
                        RedirectAttributes attributes) {
        log.info("用户名：" + username + "   密码：" + password);
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        model.addAttribute("userName", username);
        try {
            subject.login(token); //执行登录的方法，如果没有异常说明ok
            return "admin/index";
        } catch (UnknownAccountException e) {//用户名不存在
            attributes.addFlashAttribute("message", "用户名错误");
            return "redirect:/admin";
        } catch (IncorrectCredentialsException e) {//密码不存在
            attributes.addFlashAttribute("message", "密码错误");
            return "redirect:/admin";
        }

    }

    @Log("登出")
    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "admin/login";
    }
}

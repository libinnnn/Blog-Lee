package com.lee.controller;

import com.lee.annotation.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutShowController {

    @Log("显示关于我")
    @GetMapping("/about")
    public String about() {
        return "about";
    }
}

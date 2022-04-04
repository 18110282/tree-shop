package com.treeshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {
    @GetMapping("/error")
    public String errorPage(){
        return "/views/error";
    }
}

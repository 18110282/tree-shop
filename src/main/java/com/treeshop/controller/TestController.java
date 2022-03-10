package com.treeshop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {
    @Autowired
    HttpServletRequest request;
    @GetMapping("/2")
    public String home() {
        request.setAttribute("text", "Hieu o ");
        return "admin/test";
    }

//    @GetMapping("/1")
//    List<String> change(){
//        List<String> list = new ArrayList<>();
//        list.add("Hieuuuu");
//        return list;
//    }
}

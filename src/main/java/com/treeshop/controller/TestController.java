package com.treeshop.controller;


import com.treeshop.entity.OrdersEntity;
import com.treeshop.service.MailService;
import com.treeshop.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
//@RequestMapping(path = "/admin")
public class TestController {
    @Autowired
    private MailService mailService;

    @Autowired
    private OrdersService ordersService;

//    @GetMapping("/home")
//    public String home() {
//        //request.setAttribute("text", "Hieu o ");
//        return "/views/admin/home";
//    }
//
    @GetMapping("/test")
    public String login(@ModelAttribute("order") OrdersEntity ordersEntity, HttpServletRequest request, Model model) {
        //request.setAttribute("text", "Hieu o ");
//        model.addAttribute("siteURL", "http://localhost:8080");
        return "/views/test";
    }

    @GetMapping("/test/send")
    @ResponseBody
    public String test(HttpServletRequest request)throws MessagingException, IOException{
        mailService.sendMail(request);
//        ordersService.testSendMailInvoice("hieuho10022000@gmail.com", request);
        return "Done";
    }

//    @GetMapping("/1")
//    List<String> change(){
//        List<String> list = new ArrayList<>();
//        list.add("Hieuuuu");
//        return list;
//    }
}

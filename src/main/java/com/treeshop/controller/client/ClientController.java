package com.treeshop.controller.client;


import com.treeshop.entity.UserEntity;
import com.treeshop.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@RequestMapping(path = "/home")
public class ClientController {
    @Autowired
    private UsersService usersService;

    @GetMapping("/logout")
    public String logClient(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("client");
        session.invalidate();
        return "redirect:/home/list-web-product";
    }

    @PostMapping("/check-login")
    public String loginClientCheck(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "password", required = false) String password,
                                   RedirectAttributes ra,
                                   HttpSession session) {
//        String url = request.getHeader("referer");
        if (usersService.checkLogin(username, password)) {
            UserEntity client = usersService.findByUserName(username);
            if (Objects.equals(client.getRoleId(), "2")) {
                session.setAttribute("client", client);
            } else {
                ra.addFlashAttribute("errorMessage", "Không có quyền đăng nhập");
            }
        } else {
            ra.addFlashAttribute("errorMessage", "Tài khoản hoặc mật khẩu sai");
        }
//        if(url.contains("/home/no-user/cart/detail"))
//        {
//            url = "/home/" + username + "/cart/detail";
//        }
        return "redirect:/home/list-web-product";
    }
}

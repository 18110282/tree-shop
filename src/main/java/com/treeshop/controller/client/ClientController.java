package com.treeshop.controller.client;


import com.treeshop.controller.CommonController;
import com.treeshop.entity.UserEntity;
import com.treeshop.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping(path = "/home")
public class ClientController {
    private final UsersService usersService;
    private final CommonController commonController;

    @Autowired
    public ClientController(UsersService usersService, CommonController commonController) {
        this.usersService = usersService;
        this.commonController = commonController;
    }

    @GetMapping("/user/logout")
    public String logClient(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("client");
        session.removeAttribute("client_username");
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/user/check-login")
    public String checkLoginClient(@ModelAttribute("user") UserEntity user,
                                   RedirectAttributes ra,
                                   HttpSession session, HttpServletRequest request) {
        String url = commonController.getHeaderURL(request);
        String username = user.getUsername();
        String password = user.getPassword();
        if (usersService.checkLogin(username, password)) {
            UserEntity client = usersService.findByUserName(username);
            if (client.isEnabled()) {
                session.setAttribute("client", client);
                session.setAttribute("client_username", client.getUsername());
            } else {
                ra.addFlashAttribute("errorMessage", "Tài khoản chưa được xác thực, hãy tiến hành xác thực");
                return "redirect:" + url;
            }
        } else {
            ra.addFlashAttribute("errorMessage", "Sai tài khoản hoặc mật khẩu");
            return "redirect:" + url;
        }
        if (url.contains("/home/no-user/cart/detail")) {
            url = request.getContextPath() + "/home/" + username + "/cart/detail";
        }
        return "redirect:" + url;
    }

    @PostMapping("/user/register")
    private String registerClient(@ModelAttribute("user") UserEntity user,
                                  HttpServletRequest request, RedirectAttributes ra) throws MessagingException, UnsupportedEncodingException {
        String url = commonController.getHeaderURL(request);
        String siteURL = commonController.getSiteUrl(request);
        if (usersService.checkUsername(user.getUsername())) {
            ra.addFlashAttribute("registerMessage", "Tên đăng nhập " + user.getUsername() + " đã tồn tại!");
        } else {
            usersService.registerUser(user);
            usersService.sendVerificationEmail(user, siteURL);
            ra.addFlashAttribute("registerMessage", "Mời bạn xem thư xác nhận để tiến hành xác thực tài khoản " + user.getUsername());
        }
        return "redirect:" + url;
    }

    @GetMapping("/{username}/verify")
    public String verifyAccount(@PathVariable("username") String username,
                                @Param("code") String code, RedirectAttributes ra) {
        boolean verify = usersService.verifyClient(code);
        String message = verify ? "Xác thực tài khoản " + username + " thành công. Tiến hành đăng nhập!" : "Xác thực thất bại";
        ra.addFlashAttribute("verifyMessage", message);
        return "redirect:/home/list-web-product";
    }

    @PostMapping("/verify")
    public String sendVerifyAccountAgain(@RequestParam(value = "username") String username,
                                         @RequestParam(value = "email") String email,
                                         RedirectAttributes ra, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String url = commonController.getHeaderURL(request);
        UserEntity client = usersService.findByUserName(username);
        if (client == null) {
            ra.addFlashAttribute("verifyError", "Không tồn tại tài khoản " + username);
        } else {
            if (!client.getEmail().equals(email)) {
                ra.addFlashAttribute("verifyError", "Email không đúng với tài khoản " + username);
            } else {
                String siteURL = commonController.getSiteUrl(request);
                usersService.sendVerificationEmail(client, siteURL);
                ra.addFlashAttribute("registerMessage", "Vui lòng kiểm tra hòm thư để tiến hành xác thực");
            }
        }
        return "redirect:" + url;
    }

    @PostMapping("/forgot-pass")
    public String forgotPass(@RequestParam(value = "username") String username,
                             @RequestParam(value = "email") String email,
                             RedirectAttributes ra, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String url = commonController.getHeaderURL(request);
        UserEntity client = usersService.findByUserName(username);
        if (client == null) {
            ra.addFlashAttribute("verifyPassError", "Không tồn tại tài khoản " + username);
        } else {
            if (!client.getEmail().equals(email)) {
                ra.addFlashAttribute("verifyPassError", "Email không đúng với tài khoản " + username);
            } else {
                String siteURL = commonController.getSiteUrl(request);
                usersService.sendForgotPassEmail(client, siteURL);
                ra.addFlashAttribute("registerMessage", "Vui lòng kiểm tra hòm thư để tiến hành xác thực");
            }
        }
        return "redirect:" + url;
    }

}

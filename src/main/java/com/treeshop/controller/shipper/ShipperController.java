package com.treeshop.controller.shipper;

import com.treeshop.controller.CommonController;
import com.treeshop.entity.UserEntity;
import com.treeshop.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/shipper")
public class ShipperController {
    //Begin Admin Management
    @GetMapping(value = {"/login", "/logout"})
    public String log(HttpServletRequest request, HttpSession session) {
        return "/views/shipper/login";
    }

    @GetMapping("/home")
    public String showShipperHome(HttpSession session) {
        return "/views/shipper/home";
    }
}

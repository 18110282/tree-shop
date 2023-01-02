package com.treeshop.controller.shipper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showShipperHome(HttpSession session, Model model) {
        session.setAttribute("shipper_flg", 1);
        session.removeAttribute("admin_flg");
        return "/views/shipper/home";
    }

    @GetMapping("/order-delivery")
    public String showShipperOrderDelivery(HttpSession session, Model model) {
        session.setAttribute("shipper_flg", 1);
        session.removeAttribute("admin_flg");
        return "/views/shipper/order-delivery";
    }

    @GetMapping("/history-delivery")
    public String showShipperHistoryDelivery(HttpSession session, Model model) {
        session.setAttribute("shipper_flg", 1);
        session.removeAttribute("admin_flg");
        return "/views/shipper/history-delivery";
    }
}

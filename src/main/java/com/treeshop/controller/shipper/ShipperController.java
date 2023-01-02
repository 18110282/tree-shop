package com.treeshop.controller.shipper;

import com.treeshop.entity.OrdersEntity;
import com.treeshop.entity.ShipperEntity;
import com.treeshop.entity.StatusOfOrder;
import com.treeshop.entity.UserEntity;
import com.treeshop.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(path = "/shipper")
public class ShipperController {
    private final ShipperService shipperService;

    @Autowired
    public ShipperController(ShipperService shipperService) {
        this.shipperService = shipperService;
    }

    @GetMapping(value = {"/login", "/logout"})
    public String log(HttpServletRequest request, HttpSession session) {
        String url = request.getServletPath();
        Object shipperEntity = session.getAttribute("shipperEntity");
        if (url.equals("/shipper/login")) {
            if (shipperEntity == null) {
                return "/views/shipper/login";
            } else {
                return "redirect:/shipper/order-delivery";
            }
        } else if (url.equals("/shipper/logout")) {
            session = request.getSession();
            session.removeAttribute("shipperEntity");
            session.invalidate();
            return "/views/shipper/login";
        }
        return "redirect:/error";
    }

    @PostMapping("/check-login")
    public String checkLogin(@RequestParam(value = "username", required = false) String username,
                             @RequestParam(value = "password", required = false) String password,
                             RedirectAttributes ra, HttpSession session){
        if (shipperService.checkLogin(username, password)) {
            ShipperEntity shipper = shipperService.findByUsername(username);
            session.setAttribute("shipperEntity", shipper);
            return "redirect:/shipper/order-delivery";
        } else {
            ra.addFlashAttribute("errorMessage", "Tài khoản hoặc mật khẩu sai");
            return "redirect:/shipper/login";
        }
    }

    @GetMapping("/order-delivery")
    public String showShipperOrderDelivery(HttpSession session, Model model) {
        ShipperEntity shipperEntity = (ShipperEntity) session.getAttribute("shipperEntity");
        List<OrdersEntity> ordersEntityList = shipperService.findListOrderOfShipper(shipperEntity.getShipperId(), StatusOfOrder.DELIVERY);
        model.addAttribute("listOrder", ordersEntityList);
        session.setAttribute("shipper_flg", 1);
        session.removeAttribute("admin_flg");
        return "/views/shipper/order-delivery";
    }

    @GetMapping("/history-delivery")
    public String showShipperHistoryDelivery(HttpSession session, Model model) {
        ShipperEntity shipperEntity = (ShipperEntity) session.getAttribute("shipperEntity");
        List<OrdersEntity> ordersEntityList = shipperService.findListOrderOfShipper(shipperEntity.getShipperId(), StatusOfOrder.DELIVERED);
        model.addAttribute("listOrder", ordersEntityList);
        session.setAttribute("shipper_flg", 1);
        session.removeAttribute("admin_flg");
        return "/views/shipper/history-delivery";
    }
}

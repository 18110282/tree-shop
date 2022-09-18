package com.treeshop.controller.admin;

import com.treeshop.entity.StatusOfOrder;
import com.treeshop.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/admin/order")
public class OrdersController {
    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/list")
    public String showListOrder(Model model) {
        model.addAttribute("listOrder", ordersService.findAll());
        return "/views/admin/orders/list-order";
    }

    @GetMapping("/list/confirm")
    public String showListConfirmOrder(Model model) {
        model.addAttribute("listOrder", ordersService.findByStatus("Chờ xác nhận"));
        return "/views/admin/orders/confirm-order";
    }

    @GetMapping("/{orderId}/confirm")
    public String confirmOrder(@PathVariable("orderId") String orderId,
                               RedirectAttributes ra) {

        ordersService.updateStatusOfOrder(StatusOfOrder.CONFIRMED, orderId);
        ra.addFlashAttribute("alert", "Xác nhận thành công đơn đã chọn");
        return "redirect:/admin/order/list/confirm";

    }

    @PostMapping("/confirm/list")
    @ResponseBody
    public void confirmListOrder(@RequestParam(value = "arrOfOrderId[]", required = false) List<String> listWaitConfirm) {
       ordersService.updateStatusOfListOrder(StatusOfOrder.CONFIRMED, listWaitConfirm);
    }
}

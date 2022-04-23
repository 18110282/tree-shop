package com.treeshop.controller.client;


import com.treeshop.entity.OrdersEntity;
import com.treeshop.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/{username}/create-order")
    @ResponseBody
    public String createOrder(@ModelAttribute("order") OrdersEntity ordersEntity,
                              @PathVariable(name = "username") String username,
                              HttpSession session) {
        ordersService.addOrder(ordersEntity, username, session);
        return "Done";

    }
}

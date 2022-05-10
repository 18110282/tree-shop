package com.treeshop.controller.admin;

import com.treeshop.entity.OrdersEntity;
import com.treeshop.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrdersRestController {
    @Autowired
    private OrdersService ordersService;
    @GetMapping("/admin/order/detail/{orderId}")
    public OrdersEntity getOrderDetail(@PathVariable(name = "orderId") String orderId){
        ordersService.setTransientOfOrder(orderId);
        return ordersService.findOrderEntityById(orderId);
    }
}

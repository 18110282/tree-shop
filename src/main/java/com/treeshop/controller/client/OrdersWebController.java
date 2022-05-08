package com.treeshop.controller.client;


import com.treeshop.entity.LineItemEntity;
import com.treeshop.entity.OrdersEntity;
import com.treeshop.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/home")
public class OrdersWebController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/{username}/create-order")
    @ResponseBody
    public String createOrder(@ModelAttribute("order") OrdersEntity ordersEntity,
                              @PathVariable(name = "username") String username,
                              HttpSession session, HttpServletRequest request) throws MessagingException, IOException {
        ordersService.addOrder(ordersEntity, username, session);
        ordersService.sendMailInvoice(ordersEntity.getOrderId(), ordersEntity.getEmail(), request);
        return "Done";

    }

    @GetMapping("/send-invoice/order/{orderId}")
    public String order(@PathVariable(name = "orderId")String orderId,
                        Model model, HttpServletRequest request){
        OrdersEntity ordersEntity = ordersService.findOrderEntityById(orderId);
        List<LineItemEntity> lineItemEntityList = ordersService.findLineItemOfOrderId(orderId);
        if(ordersEntity.getDiscountCodeEntity() != null){
            model.addAttribute("discountPercent", ordersEntity.getDiscountCodeEntity().getDiscountPercent());
        }
        model.addAttribute("subTotal", ordersService.getSubToTalOfOrder(orderId));
        model.addAttribute("order", ordersEntity);
        model.addAttribute("lineItemList", lineItemEntityList);
        model.addAttribute("url", request.getRequestURL());
        return "/views/client/mail-invoice-detail";
    }
}

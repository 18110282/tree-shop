package com.treeshop.controller;

import com.treeshop.entity.CartEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CommonController {
    @GetMapping("/error")
    public String errorPage(){
        return "/views/error";
    }

    public int exists(String id, List<CartEntity> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getCartIdKey().getProductId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return -1;
    }
}

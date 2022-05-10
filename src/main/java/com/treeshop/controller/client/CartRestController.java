package com.treeshop.controller.client;


import com.treeshop.controller.CommonController;
import com.treeshop.entity.cart.CartEntity;
import com.treeshop.entity.UserEntity;
import com.treeshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(path = "/home")
public class CartRestController {
    @Autowired
    private CartService cartService;


    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable(name = "productId") String productId,
                                 @PathVariable(name = "quantity") Integer quantity,
                                 HttpSession session) {
        UserEntity client = (UserEntity) session.getAttribute("client");
        Integer totalPerProduct = 0;
        CommonController commonController = new CommonController();
        if (client != null) {
            String username = client.getUsername();
            cartService.updateQuantityByUsernameAndProductId(quantity, username, productId);
            CartEntity cartEntity = cartService.findByUserAndProduct(username, productId);
            totalPerProduct =  cartEntity.getTotalPerProduct();
        } else {
            List<CartEntity> cartEntityList = (List<CartEntity>) session.getAttribute("cart");
            if (cartEntityList != null) {
                int index = commonController.exists(productId, cartEntityList);
                if (index != -1) {
                    CartEntity cartEntity = cartEntityList.get(index);
                    cartEntity.setQuantity(quantity);
                    totalPerProduct = cartEntity.getTotalPerProduct();
                }
            } else {
                totalPerProduct = 0;
            }
        }
        return String.valueOf(totalPerProduct);
    }
}

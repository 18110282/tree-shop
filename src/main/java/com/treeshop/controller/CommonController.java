package com.treeshop.controller;

import com.treeshop.entity.CartEntity;
import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.entity.UserEntity;
import com.treeshop.service.CartService;
import com.treeshop.service.ProductsService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@Getter
public class CommonController {
    private List<ProductsEntity> listDiscountProduct;
    private List<ProductsEntity> listLatestProduct;
    private List<CategoryEntity> categoryEntityList;
    private Integer numberProductInCart;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CartService cartService;

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

    public void setUpCommonAttributeOfListWebProduct(HttpSession session, Model model){
        List<ProductsEntity> listDiscountProduct = productsService.findListDiscountProduct();
        List<ProductsEntity> listLatestProduct = productsService.findListLatestProduct();
        List<CategoryEntity> categoryEntityList = productsService.findAllCategory();
        UserEntity client = (UserEntity) session.getAttribute("client");
        Integer numberProductInCart = 0;
        if (client != null) {
            boolean checkUser = cartService.checkExistUser(client.getUsername());
            if (checkUser) {
                numberProductInCart = cartService.countByUser(client.getUsername());
            } else {
                List<CartEntity> cartEntityList = (List<CartEntity>) session.getAttribute("cart");
                if (cartEntityList == null) {
                    numberProductInCart = 0;
                } else {
                    cartService.saveCartFromSessionToCartEntity(cartEntityList, client.getUsername());
                    numberProductInCart = cartEntityList.size();
                }
            }
        } else {
            List<CartEntity> cartEntityList = (List<CartEntity>) session.getAttribute("cart");
            if (cartEntityList != null) {
                numberProductInCart = cartEntityList.size();
            }
        }
        model.addAttribute("numberProductInCart", numberProductInCart);
        model.addAttribute("categoryList", categoryEntityList);
        model.addAttribute("listLatestProduct", listLatestProduct);
        model.addAttribute("listDiscountProduct", listDiscountProduct);
        this.listDiscountProduct = listDiscountProduct;
        this.listLatestProduct = listLatestProduct;
        this.categoryEntityList = categoryEntityList;
        this.numberProductInCart = numberProductInCart;
    }

}

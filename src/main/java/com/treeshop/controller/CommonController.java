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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Getter
public class CommonController {
    private List<ProductsEntity> listDiscountProduct;
    private List<ProductsEntity> listLatestProduct;
    private List<CategoryEntity> categoryEntityList;
    private int numberProductInCart;

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

    public int getNumberProductInCart(HttpSession session) {
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
                    //If in cart table doesn't exist user, we will add cart from session to cart table with their username
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
        return numberProductInCart;
    }

    public void setUpCommonAttributeOfListWebProduct(Page<ProductsEntity> productsEntityPage, HttpSession session, Model model){
        List<ProductsEntity> listAllProduct = productsEntityPage.getContent();
        Long totalProducts = productsEntityPage.getTotalElements();
        List<ProductsEntity> listDiscountProduct = productsService.findListDiscountProduct();
        List<ProductsEntity> listLatestProduct = productsService.findListLatestProduct();
        List<CategoryEntity> categoryEntityList = productsService.findAllCategory();
        Integer maxPrice = productsService.findMaxPriceInAllProduct();
        Integer numberProductInCart = this.getNumberProductInCart(session);
        model.addAttribute("numberProductInCart", numberProductInCart);
        model.addAttribute("categoryList", categoryEntityList);
        model.addAttribute("listLatestProduct", listLatestProduct);
        model.addAttribute("listDiscountProduct", listDiscountProduct);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("listProduct", listAllProduct);
//        this.listDiscountProduct = listDiscountProduct;
//        this.listLatestProduct = listLatestProduct;
//        this.categoryEntityList = categoryEntityList;
//        this.numberProductInCart = numberProductInCart;
    }

}

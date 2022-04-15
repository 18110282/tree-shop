package com.treeshop.controller.client;


import com.treeshop.controller.CommonController;
import com.treeshop.entity.*;
import com.treeshop.service.CartService;
import com.treeshop.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductsWebController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CommonController commonController;

    @GetMapping("/home/list-web-product")
    public String showListWebProduct(Model model, HttpSession session) {
        return showListProductByPage(model, 1, session);
    }

    @GetMapping("/home/list-web-product/{page}")
    public String showListProductByPage(Model model,
                                        @PathVariable("page") Integer currentPage,
                                        HttpSession session) {
        //List All Product
        Page<ProductsEntity> productsEntityPage = productsService.findAll(currentPage);
        List<ProductsEntity> listAllProduct = productsEntityPage.getContent();
        Long totalProducts = productsEntityPage.getTotalElements();
        Integer totalPages = productsEntityPage.getTotalPages();
        commonController.setUpCommonAttributeOfListWebProduct(session, model);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listProduct", listAllProduct);
        return "/views/client/list-web-product";
    }

    @GetMapping("/home/add-product/{productId}")
    public String addCart(@PathVariable(name = "productId") String productId,
                          HttpServletRequest request,
                          HttpSession session) {
        String previousUrl = request.getHeader("referer");
        CartEntity cartEntity = new CartEntity();
        CartIdKey cartIdKey = new CartIdKey();
        UserEntity client = (UserEntity) session.getAttribute("client");
        Integer price = productsService.findDiscountPriceByProductId(productId);
        if (client == null) {
            if (session.getAttribute("cart") == null) {
                List<CartEntity> cartEntityList = new ArrayList<>();
                cartService.setCartEntityOfNoUserInCartSession(cartEntityList, cartEntity, cartIdKey, productId, price);
                session.setAttribute("cart", cartEntityList);
            } else {
                List<CartEntity> cartEntityList = (List<CartEntity>) session.getAttribute("cart");
                int index = commonController.exists(productId, cartEntityList);
                if (index == -1) {
                    cartService.setCartEntityOfNoUserInCartSession(cartEntityList, cartEntity, cartIdKey, productId, price);
                } else {
                    Integer quantity = cartEntityList.get(index).getQuantity();
                    cartEntityList.get(index).setQuantity(quantity + 1);
                }
                session.setAttribute("cart", cartEntityList);
            }
        } else {
            String username = client.getUsername();
            cartEntity = cartService.setCartEntityOfUserInCartDB(cartEntity, cartIdKey, username, productId, price);
            cartService.saveCart(cartEntity);
        }
        return "redirect:" + previousUrl;
    }
}

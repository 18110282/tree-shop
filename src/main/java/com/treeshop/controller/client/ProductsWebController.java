package com.treeshop.controller.client;


import com.treeshop.controller.CommonController;
import com.treeshop.entity.*;
import com.treeshop.entity.cart.CartEntity;
import com.treeshop.entity.cart.CartIdKey;
import com.treeshop.service.CartService;
import com.treeshop.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/home")
public class ProductsWebController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CommonController commonController;

    @GetMapping("/list-web-product")
    public String showListWebProduct(Model model, HttpSession session) {
        return showListProductByPage(model, 1, session);
    }

    @GetMapping("/list-web-product/{page}")
    public String showListProductByPage(Model model,
                                        @PathVariable("page") Integer currentPage,
                                        HttpSession session) {
        //List All Product
        Page<ProductsEntity> productsEntityPage = productsService.findAll(currentPage);
        Integer totalPages = productsEntityPage.getTotalPages();
        commonController.setUpCommonAttributeOfListWebProduct(productsEntityPage, session, model);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "/views/client/list-web-product";
    }

    @GetMapping("/add-product/{productId}/{quantity}")
    public String addCart(@PathVariable(name = "productId") String productId,
                          @PathVariable(name = "quantity") Integer quantityUrl,
                          HttpServletRequest request,
                          HttpSession session) {
        String previousUrl = commonController.getHeaderURL(request);
        CartEntity cartEntity = new CartEntity();
        CartIdKey cartIdKey = new CartIdKey();
        UserEntity client = (UserEntity) session.getAttribute("client");
        Integer price = productsService.findDiscountPriceByProductId(productId);
        if (client == null) {
            if (session.getAttribute("cart") == null) {
                List<CartEntity> cartEntityList = new ArrayList<>();
                cartService.setCartEntityOfNoUserInCartSession(cartEntityList, cartEntity, cartIdKey, productId, price, quantityUrl);
                session.setAttribute("cart", cartEntityList);
            } else {
                List<CartEntity> cartEntityList = (List<CartEntity>) session.getAttribute("cart");
                int index = commonController.exists(productId, cartEntityList);
                if (index == -1) {
                    cartService.setCartEntityOfNoUserInCartSession(cartEntityList, cartEntity, cartIdKey, productId, price, quantityUrl);
                } else {
                    Integer quantity = cartEntityList.get(index).getQuantity();
                    cartEntityList.get(index).setQuantity(quantity + quantityUrl);
                }
                session.setAttribute("cart", cartEntityList);
            }
        } else {
            String username = client.getUsername();
            cartEntity = cartService.setCartEntityOfUserInCartDB(cartEntity, cartIdKey, username, productId, price, quantityUrl);
            cartService.saveCart(cartEntity);
        }
        return "redirect:" + previousUrl;
    }

    @GetMapping("/product/{productId}/detail")
    public String showProductDetail(@PathVariable(name="productId") String productId, Model model,
                                    HttpSession session){
        Integer numberProductInCart = commonController.getNumberProductInCart(session);
        ProductsEntity productsEntity = productsService.findByProductId(productId);
        String categoryId = productsEntity.getCategoryId();
        List<ProductsEntity> productsEntityList = productsService.findRelatedProduct(categoryId);
        model.addAttribute("listRelatedProduct", productsEntityList);
        model.addAttribute("product", productsEntity);
        model.addAttribute("numberProductInCart", numberProductInCart);
        return "/views/client/product-detail";
    }
}

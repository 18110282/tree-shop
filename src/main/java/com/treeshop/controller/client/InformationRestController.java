package com.treeshop.controller.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.treeshop.entity.OrdersEntity;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.entity.StatusOfOrder;
import com.treeshop.entity.cart.CartEntity;
import com.treeshop.entity.cart.CartIdKey;
import com.treeshop.entity.lineitem.LineItemEntity;
import com.treeshop.entity.review.ReviewsEntity;
import com.treeshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/home")
public class InformationRestController {
    private final ProductsService productsService;
    private final OrdersService ordersService;
    private final UsersService usersService;
    private final CartService cartService;
    private final ReviewProductService reviewProductService;

    @Autowired
    public InformationRestController(ProductsService productsService, OrdersService ordersService, UsersService usersService, CartService cartService, ReviewProductService reviewProductService) {
        this.productsService = productsService;
        this.ordersService = ordersService;
        this.usersService = usersService;
        this.cartService = cartService;
        this.reviewProductService = reviewProductService;
    }


    @PostMapping("/save/{username}/password")
    public boolean savePasswordOfClient(@PathVariable("username") String username,
                                        @RequestParam(value = "oldP") String oldPassword,
                                        @RequestParam(value = "newP") String newPassword) {
        return usersService.savePasswordOfClient(username, oldPassword, newPassword);
    }

    @PostMapping("/buy-again/{username}/{orderId}")
    public List<LineItemEntity> buyOrderAgain(@PathVariable("orderId") String orderId) {
        List<LineItemEntity> lineItemEntityListAll = lineItemEntityListAll(orderId);

        return lineItemEntityListAll.stream()
                .filter(l -> productsService.checkProductIsDelete(l.getProductsEntity().getProductId()))
                .collect(Collectors.toList());
    }

    @PostMapping("/{username}/add-item-to-cart/{orderId}")
    public boolean addItemToCart(@RequestBody String body,
                                 @PathVariable("username") String username,
                                 @PathVariable("orderId") String orderId) {
        boolean isSuccess = false;
        try {
            Gson gson = new Gson();
            Type lineItemListType = new TypeToken<List<LineItemEntity>>() {}.getType();
            List<LineItemEntity> lineItemHaveProductIsDelete = gson.fromJson(body, lineItemListType);

            List<LineItemEntity> lineItemEntityListAll = lineItemEntityListAll(orderId);

            int sizeOfLineItemIsDelete = lineItemHaveProductIsDelete.size();
            if (sizeOfLineItemIsDelete != 0) {
                lineItemEntityListAll.removeAll(lineItemHaveProductIsDelete);
            }


            for (LineItemEntity lineItem : lineItemEntityListAll) {
                CartIdKey cartIdKey = new CartIdKey();
                CartEntity cartEntity = new CartEntity();
                ProductsEntity productsEntity = lineItem.getProductsEntity();
                cartEntity = cartService.setCartEntityOfUserInCartDB(cartEntity, cartIdKey, username, productsEntity.getProductId(), productsEntity.getDiscountPrice(), lineItem.getQuantity());
                cartService.saveCart(cartEntity);
            }
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @PostMapping("/information/cancel-order/{orderId}")
    public boolean cancelOrderForClient(@PathVariable("orderId") String orderId) {
        boolean isSuccess = false;
        try {
            if (!ordersService.existsByStatusAndOrderId(StatusOfOrder.CONFIRMED, orderId)) {
                ordersService.updateStatusOfOrder(StatusOfOrder.CANCELLED, orderId);
                isSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @GetMapping("/{orderId}/line-item/detail")
    public List<LineItemEntity> findLineItemByOrderId(@PathVariable("orderId") String orderId) {
        return lineItemEntityListAll(orderId);
    }

    @GetMapping("/{orderId}/read/review")
    public List<ReviewsEntity> findReviewByOrderId(@PathVariable("orderId") String orderId) {
        OrdersEntity ordersEntity = ordersService.findOrderEntityById(orderId);
        return ordersEntity.getReviewsEntityList();
    }

    @PostMapping("/{orderId}/review")
    public boolean reviewProduct(@RequestBody String body,
                                 @PathVariable("orderId") String orderId) {
        boolean isSuccess = false;
        try {
            Gson gson = new Gson();
            Type reviewType = new TypeToken<List<ReviewsEntity>>() {}.getType();
            List<ReviewsEntity> reviewsEntityList = gson.fromJson(body, reviewType);
            reviewProductService.addReviewForProduct(reviewsEntityList, orderId);
            isSuccess = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return isSuccess;
    }

    private List<LineItemEntity> lineItemEntityListAll(String orderId){
        OrdersEntity ordersEntity = ordersService.findOrderEntityById(orderId);
        return  ordersEntity.getLineItemEntityList();
    }
}

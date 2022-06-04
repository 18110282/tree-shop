package com.treeshop.service;

import com.treeshop.entity.cart.CartEntity;

import java.util.List;


public interface CheckoutService {
    Integer getSubTotal();

    Integer getTotal();

    void checkMoneyInCart(List<CartEntity> cartEntityList, Integer discountPercent);

    boolean checkUserInCart(String username);
}

package com.treeshop.service;

import com.treeshop.entity.CartEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {
    private Integer subTotal;
    private Integer total;

    public Integer getSubTotal() {
        return subTotal;
    }

    public Integer getTotal() {
        return total;
    }

    public void checkMoneyInCart(List<CartEntity> cartEntityList, Integer discoutPercent){
        int subTotal = 0;
        for (CartEntity cartEntity: cartEntityList) {
            subTotal = subTotal + cartEntity.getTotalPerProduct();
        }
        this.subTotal = subTotal;
        this.total = subTotal * (100-discoutPercent)/100;
    }
}

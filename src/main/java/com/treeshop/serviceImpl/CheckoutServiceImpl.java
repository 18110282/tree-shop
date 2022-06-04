package com.treeshop.serviceImpl;

import com.treeshop.dao.CartRepository;
import com.treeshop.entity.cart.CartEntity;
import com.treeshop.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    private final CartRepository cartRepository;
    private Integer subTotal;
    private Integer total;

    @Autowired
    public CheckoutServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public Integer getTotal() {
        return total;
    }

    public void checkMoneyInCart(List<CartEntity> cartEntityList, Integer discountPercent){
        int subTotal = 0;
        for (CartEntity cartEntity: cartEntityList) {
            subTotal = subTotal + cartEntity.getTotalPerProduct();
        }
        this.subTotal = subTotal;
        this.total = subTotal * (100-discountPercent)/100;
    }

    public boolean checkUserInCart(String username){
        return cartRepository.existsByCartIdKey_Username(username);
    }
}

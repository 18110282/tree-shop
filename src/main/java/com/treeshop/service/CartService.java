package com.treeshop.service;


import com.treeshop.dao.CartRepository;
import com.treeshop.entity.CartEntity;
import com.treeshop.entity.CartIdKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CartService {
    @Autowired
    private CartRepository cartRepository;


    public void saveCart(CartEntity cartEntity) {
        cartRepository.save(cartEntity);
    }

    public void saveCartFromSessionToCartEntity(List<CartEntity> cartEntityList, String username){
        for (CartEntity cartEntity : cartEntityList) {
            cartEntity.getCartIdKey().setUsername(username);
            cartRepository.save(cartEntity);
        }
    }

    public boolean checkExistUser(String username) {
        return cartRepository.existsByCartIdKey_Username(username);
    }

    public boolean checkQuantity(String username, Integer quantity) {
        return cartRepository.existsByCartIdKey_UsernameAndQuantity(username, quantity);
    }

    public CartEntity findByUserAndProduct(String username, String productId) {
        return cartRepository.findByCartIdKey_UsernameAndCartIdKey_ProductId(username, productId);
    }

    public Integer countByUser(String username) {
        return cartRepository.countByCartIdKey_Username(username);
    }

    public List<CartEntity> findListCartByUsername(String username) {
        return cartRepository.findCartEntitiesByCartIdKey_Username(username);
    }

    public List<CartEntity> findListCartByUsernameEmptyQuantity(String username) {
        return cartRepository.getProductIdEmptyQuantity(username);
    }

    public void updateQuantityByUsernameAndProductId(Integer quantity, String username, String productId) {
        cartRepository.updateQuantity(quantity, username, productId);
    }

    public void deleteItem(String username, String productId) {
        cartRepository.deleteCartEntityByCartIdKey_UsernameAndCartIdKey_ProductId(username, productId);
    }

    public void setCartEntityOfNoUserInCartSession(List<CartEntity> cartEntityList, CartEntity cartEntity, CartIdKey cartIdKey, String productId, Integer price, Integer quantityUrl){
        cartIdKey.setProductId(productId);
        cartIdKey.setUsername("");
        cartEntity.setCartIdKey(cartIdKey);
        cartEntity.setQuantity(quantityUrl);
        cartEntity.setPrice(price);
        cartEntityList.add(cartEntity);
    }
    public CartEntity setCartEntityOfUserInCartDB(CartEntity cartEntity, CartIdKey cartIdKey, String username, String productId, Integer price, Integer quantityUrl){
        if (cartRepository.existsByCartIdKey_UsernameAndCartIdKey_ProductId(username, productId)) {
            cartEntity = cartRepository.findByCartIdKey_UsernameAndCartIdKey_ProductId(username, productId);
            cartEntity.setQuantity(cartEntity.getQuantity() + quantityUrl);
        } else {
            cartIdKey.setUsername(username);
            cartIdKey.setProductId(productId);
            cartEntity.setCartIdKey(cartIdKey);
            cartEntity.setPrice(price);
            cartEntity.setQuantity(quantityUrl);
        }
        return cartEntity;
    }

    public CartEntity compareQuantityInStockVsCart(List<CartEntity> cartEntityList){
        CartEntity cart = new CartEntity();
        int quantityInCart;
        int quantityInStock;
        for (CartEntity cartEntity: cartEntityList) {
            quantityInStock = cartEntity.getProductsEntity().getUnitInStock();
            quantityInCart = cartEntity.getQuantity();
            if(quantityInCart > quantityInStock){
                cart = cartEntity;
                break;
            }
        }
        return cart;
    }
}

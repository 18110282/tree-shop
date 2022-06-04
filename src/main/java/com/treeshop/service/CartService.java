package com.treeshop.service;

import com.treeshop.entity.cart.CartEntity;
import com.treeshop.entity.cart.CartIdKey;
import java.util.List;

public interface CartService {

    void saveCart(CartEntity cartEntity);

    void saveCartFromSessionToCartEntity(List<CartEntity> cartEntityList, String username);

    boolean checkExistUser(String username);

    boolean checkQuantity(String username, Integer quantity);

    CartEntity findByUserAndProduct(String username, String productId);

    Integer countByUser(String username);

    List<CartEntity> findListCartByUsername(String username);

    List<CartEntity> findListCartByUsernameEmptyQuantity(String username);

    void updateQuantityByUsernameAndProductId(Integer quantity, String username, String productId);

    void deleteItem(String username, String productId);

    void setCartEntityOfNoUserInCartSession(List<CartEntity> cartEntityList, CartEntity cartEntity, CartIdKey cartIdKey, String productId, Integer price, Integer quantityUrl);

    CartEntity setCartEntityOfUserInCartDB(CartEntity cartEntity, CartIdKey cartIdKey, String username, String productId, Integer price, Integer quantityUrl);

    CartEntity compareQuantityInStockVsCart(List<CartEntity> cartEntityList);

    boolean checkUsedCodeIdOfUser(String username, String codeId);
}

package com.treeshop.dao;

import com.treeshop.entity.CartEntity;
import com.treeshop.entity.CartIdKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, CartIdKey> {
        void deleteByCartIdKey_Username(String username);
        boolean existsByCartIdKey_UsernameAndCartIdKey_ProductId(String username, String productId);
        boolean existsByCartIdKey_Username(String username);
        boolean existsByCartIdKey_UsernameAndQuantity(String username, Integer quantity);
        CartEntity findByCartIdKey_UsernameAndCartIdKey_ProductId(String username, String productId);
        Integer countByCartIdKey_Username(String username);
        List<CartEntity> findCartEntitiesByCartIdKey_Username(String username);

        @Query("update CartEntity c set c.quantity = :quantity where c.cartIdKey.username = :username " +
                "and c.cartIdKey.productId = :productId")
        @Modifying
        void updateQuantity(@Param("quantity") Integer quantity, @Param("username") String username, @Param("productId")String productId);

        @Modifying
        void deleteCartEntityByCartIdKey_UsernameAndCartIdKey_ProductId(String username, String productId);

        @Query("select c from CartEntity c where c.cartIdKey.username = :username and c.quantity = 0")
        List<CartEntity> getProductIdEmptyQuantity(@Param("username")String username);

}

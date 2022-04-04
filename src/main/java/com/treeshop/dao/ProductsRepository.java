package com.treeshop.dao;

import com.treeshop.entity.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProductsRepository extends JpaRepository<ProductsEntity, String> {
    ProductsEntity findByProductId(String productId);
    boolean existsByProductId(String productId);
}

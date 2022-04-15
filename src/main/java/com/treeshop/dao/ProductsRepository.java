package com.treeshop.dao;

import com.treeshop.entity.ProductsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends PagingAndSortingRepository<ProductsEntity, String> {
    //admin
    @Query("select p from  ProductsEntity p ")
    List<ProductsEntity> findAllQuery();
    @Query("select p from ProductsEntity  p where p.discountPercent <> 0")
    List<ProductsEntity> findDiscountProduct();
    ProductsEntity findByProductId(String productId);
    boolean existsByProductId(String productId);
    //client
    Page<ProductsEntity> findAll(Pageable pageable);
    @Query("select p.price from  ProductsEntity p where p.productId = :productId ")
    Integer findPriceByProductId(@Param("productId") String productId);
    @Query("select p.discountPercent from  ProductsEntity p where p.productId = :productId ")
    Integer findDiscountPercentByProductId(@Param("productId") String productId);
    List<ProductsEntity> findAllByOrderByCreateDateDesc();
}
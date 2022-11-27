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
public interface ProductsRepository extends PagingAndSortingRepository<ProductsEntity, String>, ProductsCustomRepository {
    //admin
    @Query("select p from  ProductsEntity p where p.enabled = true")
    List<ProductsEntity> findAllQuery();

    @Query("select p from ProductsEntity  p where p.discountPercent <> 0 and p.enabled = true")
    List<ProductsEntity> findDiscountProduct();

    ProductsEntity findByProductId(String productId);

    boolean existsByProductId(String productId);

    //client
    Page<ProductsEntity> findAllByEnabledIsTrue(Pageable pageable);

    @Query("select p.price from  ProductsEntity p where p.productId = :productId and p.enabled = true")
    Integer findPriceByProductId(@Param("productId") String productId);

    @Query("select p.discountPercent from  ProductsEntity p where p.productId = :productId and p.enabled = true")
    Integer findDiscountPercentByProductId(@Param("productId") String productId);

    @Query("select p from ProductsEntity p where concat(p.productName, ' ', p.categoryEntity.categoryName, ' ', p.price, ' ', p.description) like %:keyword% and p.enabled = true")
    Page<ProductsEntity> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    //List random 4 related products with main product
//    @Query(value = "select * from products p where p.category_id = :categoryId and p.enabled = true order by RAND() limit 4", nativeQuery = true)
//    List<ProductsEntity> findRandomProductInSameCategory(@Param("categoryId") String categoryId);
    List<ProductsEntity> findRandomProductInSameCategory(String categoryId);

    @Query("select max(p.price) from ProductsEntity p where p.enabled = true")
    Integer findMaxPrice();

    Page<ProductsEntity> findAllByPriceBetweenAndEnabledIsTrue(Integer min, Integer max, Pageable page);

    List<ProductsEntity> findAllByEnabledIsTrueOrderByCreateDateDesc();

    List<ProductsEntity> findTop6ByEnabledIsTrueOrderByCreateDateDesc();

    List<ProductsEntity> findTop6ByEnabledIsTrueOrderByVisitDesc();

    boolean existsByProductIdAndEnabled(String productId, boolean enabled);


}

package com.treeshop.dao;

import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.ProductsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, String> {
    List<CategoryEntity> findAll();

    @Query("select listP from CategoryEntity c join c.productsEntityList listP where c.categoryId = :categoryId")
    Page<ProductsEntity> findListProductInCategory(@Param("categoryId")String categoryId, Pageable pageable);
}

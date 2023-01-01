package com.treeshop.service;

import com.treeshop.entity.ProductsEntity;
import org.springframework.data.domain.Page;

import java.util.List;


public interface SearchService {
    Page<ProductsEntity> searchProduct(String keyword, Integer number);

    Page<ProductsEntity> searchProductByPrice(Integer minPrice, Integer maxPrice, Integer number);

    List<ProductsEntity> searchProductByCondition(Integer minPrice, Integer maxPrice, String weight, String height);

}

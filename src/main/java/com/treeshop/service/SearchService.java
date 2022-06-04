package com.treeshop.service;

import com.treeshop.entity.ProductsEntity;
import org.springframework.data.domain.Page;


public interface SearchService {
    Page<ProductsEntity> searchProduct(String keyword, Integer number);

    Page<ProductsEntity> searchProductByPrice(Integer minPrice, Integer maxPrice, Integer number);

}

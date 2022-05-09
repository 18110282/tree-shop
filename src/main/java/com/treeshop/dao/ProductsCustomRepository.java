package com.treeshop.dao;


import com.treeshop.entity.ProductsEntity;

import java.util.List;

public interface ProductsCustomRepository {
    List<ProductsEntity> findRandomProductInSameCategory(String categoryId);
}

package com.treeshop.dao;


import com.treeshop.entity.ProductsEntity;

import java.util.List;

public interface LineItemCustomRepository {
    List<ProductsEntity> findTopSellProduct();
}

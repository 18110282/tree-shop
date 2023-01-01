package com.treeshop.dao;


import com.treeshop.entity.ProductsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductsCustomRepository {
    List<ProductsEntity> findRandomProductInSameCategory(String categoryId);
    List<ProductsEntity> searchByCondition(Integer max, Integer min, String weight, String height);
}

package com.treeshop.service;

import com.treeshop.entity.ProductsEntity;

import java.util.List;

public interface HomeService {
    List<ProductsEntity> findTopEightBestSellingProduct();

    List<ProductsEntity> findTopSixLatestProduct(Integer slide);

    List<ProductsEntity> findTopSixVisitProduct(Integer slide);

    List<ProductsEntity> findTopSixReviewProduct(Integer slide);

    List<ProductsEntity> handleSlideOfTopSixProduct(Integer slide, List<ProductsEntity> productsEntityList);
}

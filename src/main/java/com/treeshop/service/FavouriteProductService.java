package com.treeshop.service;

import com.treeshop.entity.favouriteproduct.FavouriteProductEntity;
import com.treeshop.entity.favouriteproduct.FavouriteProductIdKey;

import java.util.List;

public interface FavouriteProductService {
    boolean checkExistsFavouriteProductIdKey(FavouriteProductIdKey favouriteProductIdKey);
    void saveFavouriteProduct(FavouriteProductEntity favouriteProductEntity);
    void deleteFavouriteProduct(FavouriteProductEntity favouriteProductEntity);
    List<FavouriteProductEntity> findByFavouriteProductByUsername(String username);
}

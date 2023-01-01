package com.treeshop.dao;

import com.treeshop.entity.favouriteproduct.FavouriteProductEntity;
import com.treeshop.entity.favouriteproduct.FavouriteProductIdKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteProductRepository extends CrudRepository<FavouriteProductEntity, FavouriteProductIdKey> {
    boolean existsByFavouriteProductIdKey(FavouriteProductIdKey favouriteProductIdKey);
    List<FavouriteProductEntity> findByFavouriteProductIdKey_Username(String username);
}

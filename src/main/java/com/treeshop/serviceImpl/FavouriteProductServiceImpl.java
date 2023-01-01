package com.treeshop.serviceImpl;

import com.treeshop.dao.FavoriteProductRepository;
import com.treeshop.entity.favouriteproduct.FavouriteProductEntity;
import com.treeshop.entity.favouriteproduct.FavouriteProductIdKey;
import com.treeshop.service.FavouriteProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FavouriteProductServiceImpl implements FavouriteProductService {
    private final FavoriteProductRepository favoriteProductRepository;

    @Autowired
    public FavouriteProductServiceImpl(FavoriteProductRepository favoriteProductRepository) {
        this.favoriteProductRepository = favoriteProductRepository;
    }

    @Override
    public boolean checkExistsFavouriteProductIdKey(FavouriteProductIdKey favouriteProductIdKey) {
        return favoriteProductRepository.existsByFavouriteProductIdKey(favouriteProductIdKey);
    }

    @Override
    public void saveFavouriteProduct(FavouriteProductEntity favouriteProductEntity) {
        favouriteProductEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        favoriteProductRepository.save(favouriteProductEntity);
    }

    @Override
    public void deleteFavouriteProduct(FavouriteProductEntity favouriteProductEntity) {
        favoriteProductRepository.delete(favouriteProductEntity);
    }

    @Override
    public List<FavouriteProductEntity> findByFavouriteProductByUsername(String username) {
        return favoriteProductRepository.findByFavouriteProductIdKey_Username(username);
    }
}

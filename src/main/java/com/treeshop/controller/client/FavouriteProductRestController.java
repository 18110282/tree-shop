package com.treeshop.controller.client;

import com.treeshop.entity.favouriteproduct.FavouriteProductEntity;
import com.treeshop.entity.favouriteproduct.FavouriteProductIdKey;
import com.treeshop.service.FavouriteProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favourite")
public class FavouriteProductRestController {
    private final FavouriteProductService favouriteProductService;

    public FavouriteProductRestController(FavouriteProductService favouriteProductService) {
        this.favouriteProductService = favouriteProductService;
    }

    @GetMapping("/check-exist/{username}/{productId}")
    public boolean checkExistFavouriteProduct(@PathVariable("username") String username,
                                              @PathVariable("productId") String productId){
        FavouriteProductIdKey favouriteProductIdKey = new FavouriteProductIdKey();
        favouriteProductIdKey.setProductId(productId);
        favouriteProductIdKey.setUsername(username);

        return favouriteProductService.checkExistsFavouriteProductIdKey(favouriteProductIdKey);
    }

    @PostMapping("/add/{username}/{productId}")
    public boolean addFavouriteProduct(@PathVariable("username") String username,
                                       @PathVariable("productId") String productId){
        boolean isSuccess = false;
        try {
            FavouriteProductIdKey favouriteProductIdKey = new FavouriteProductIdKey();
            favouriteProductIdKey.setProductId(productId);
            favouriteProductIdKey.setUsername(username);

            FavouriteProductEntity favouriteProductEntity = new FavouriteProductEntity();
            favouriteProductEntity.setFavouriteProductIdKey(favouriteProductIdKey);

            favouriteProductService.saveFavouriteProduct(favouriteProductEntity);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @PostMapping("/delete/{username}/{productId}")
    public boolean deleteFavouriteProduct(@PathVariable("username") String username,
                                       @PathVariable("productId") String productId){
        boolean isSuccess = false;
        try {
            FavouriteProductIdKey favouriteProductIdKey = new FavouriteProductIdKey();
            favouriteProductIdKey.setProductId(productId);
            favouriteProductIdKey.setUsername(username);

            FavouriteProductEntity favouriteProductEntity = new FavouriteProductEntity();
            favouriteProductEntity.setFavouriteProductIdKey(favouriteProductIdKey);

            favouriteProductService.deleteFavouriteProduct(favouriteProductEntity);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
}

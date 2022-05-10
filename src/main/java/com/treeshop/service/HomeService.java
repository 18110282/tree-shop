package com.treeshop.service;

import com.treeshop.dao.LineItemRepository;
import com.treeshop.dao.ProductsRepository;
import com.treeshop.entity.ProductsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private LineItemRepository lineItemRepository;

    public List<ProductsEntity> findTopEightBestSellingProduct(){
        return lineItemRepository.findTopSellProduct();
    }

    public List<ProductsEntity> findTopSixLatestProduct(Integer slide){
        List<ProductsEntity> productsEntityList = productsRepository.findTop6ByEnabledIsTrueOrderByCreateDateDesc();
        return this.handleSlideOfTopSixProduct(slide, productsEntityList);
    }

    public List<ProductsEntity> findTopSixVisitProduct(Integer slide){
        List<ProductsEntity> productsEntityList = productsRepository.findTop6ByEnabledIsTrueOrderByVisitDesc();
        return this.handleSlideOfTopSixProduct(slide, productsEntityList);
    }

    public List<ProductsEntity> handleSlideOfTopSixProduct(Integer slide, List<ProductsEntity> productsEntityList){
        List<ProductsEntity> result = new ArrayList<>();
        if(slide == 1){
            for(int i = 0; i < 3; i++){
                if(i >= productsEntityList.size()){
                    break;
                }
                result.add(productsEntityList.get(i));
            }
        }
        else {
            for(int i = 3; i < 6; i++){
                if(i >= productsEntityList.size()){
                    break;
                }
                result.add(productsEntityList.get(i));
            }
        }
        return result;
    }
}

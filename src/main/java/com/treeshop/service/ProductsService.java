package com.treeshop.service;

import com.treeshop.dao.CategoryRepository;
import com.treeshop.dao.ProductsRepository;
import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.ProductsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductsService {
    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public List<ProductsEntity> findAll(){
        return  productsRepository.findAll();
    }

    public ProductsEntity saveProduct(ProductsEntity productsEntity){
        return productsRepository.save(productsEntity);
    }

    public ProductsEntity findByProductId(String productId){
        return  productsRepository.findByProductId(productId);
    }

    public boolean checkProductId(String productId){
        return productsRepository.existsByProductId(productId);
    }

    public void deleteProduct(String productId){
        productsRepository.deleteById(productId);
    }

    public List<CategoryEntity> findAllCategory(){
        return categoryRepository.findAll();
    }

}

package com.treeshop.service;

import com.treeshop.dao.ProductsRepository;
import com.treeshop.entity.ProductsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class SearchService {
    @Autowired
    private ProductsRepository productsRepository;

    public Page<ProductsEntity> searchProduct(String keyword, Integer number){
        Pageable pageable = PageRequest.of(number - 1, 6);
        return productsRepository.searchProducts(keyword, pageable);
    }
}

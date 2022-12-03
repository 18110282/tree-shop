package com.treeshop.serviceImpl;

import com.treeshop.dao.ProductsRepository;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    private final ProductsRepository productsRepository;

    @Autowired
    public SearchServiceImpl(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Page<ProductsEntity> searchProduct(String keyword, Integer number) {
        Pageable pageable = PageRequest.of(number - 1, 9);
        return productsRepository.searchProducts(keyword, pageable);
    }

    public Page<ProductsEntity> searchProductByPrice(Integer minPrice, Integer maxPrice, Integer number) {
        Pageable pageable = PageRequest.of(number - 1, 9);
        return productsRepository.findAllByPriceBetweenAndEnabledIsTrue(minPrice, maxPrice, pageable);
    }
}

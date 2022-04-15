package com.treeshop.service;


import com.treeshop.dao.CategoryRepository;
import com.treeshop.entity.ProductsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Page<ProductsEntity> findListProductInCategory(String categoryId,Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 3);
        return categoryRepository.findListProductInCategory(categoryId, pageable);
    }
}

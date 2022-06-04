package com.treeshop.service;

import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.ProductsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.util.List;


public interface CategoryService {
    List<CategoryEntity> findAll();

    CategoryEntity findCategoryById(String categoryId);

    boolean checkCategoryId(String categoryId);

    void deleteCategory(String categoryId);

    void saveCategory(CategoryEntity categoryEntity, MultipartFile multipartFile) throws IOException;

    Page<ProductsEntity> findListProductInCategory(String categoryId, Integer pageNumber);
}

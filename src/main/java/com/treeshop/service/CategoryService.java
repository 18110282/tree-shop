package com.treeshop.service;


import com.treeshop.dao.CategoryRepository;
import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.ProductsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommonService commonService;

    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    public CategoryEntity findCategoryById(String categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }

    public boolean checkCategoryId(String categoryId) {
        return categoryRepository.existsByCategoryId(categoryId);
    }

    public void deleteCategory(String categoryId){
        categoryRepository.deleteById(categoryId);
    }

    public void saveCategory(CategoryEntity categoryEntity, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String categoryId = categoryEntity.getCategoryId();
        categoryEntity.setCreateDay(Timestamp.valueOf(LocalDateTime.now()));
        if (!fileName.equals("")) {
            String uploadDir = "./dynamic-resources/category-imgs/" + categoryId;
            Path uploadPath = Paths.get(uploadDir);
            commonService.processFile(uploadPath,multipartFile,fileName);
        }
        else {
            CategoryEntity categoryEntity1 = this.findCategoryById(categoryId);
            if(categoryEntity1 != null) {
                fileName = categoryEntity1.getImageUrl();
            }
        }
        categoryEntity.setImageUrl(fileName);
        categoryRepository.save(categoryEntity);
    }

    public Page<ProductsEntity> findListProductInCategory(String categoryId, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 12);
        return categoryRepository.findListProductInCategory(categoryId, pageable);
    }
}

package com.treeshop.dao;

import com.treeshop.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<CategoryEntity, String> {
    List<CategoryEntity> findAll();
}

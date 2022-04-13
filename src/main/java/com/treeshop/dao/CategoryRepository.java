package com.treeshop.dao;

import com.treeshop.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, String> {
    List<CategoryEntity> findAll();
}

package com.treeshop.dao;

import com.treeshop.entity.DiscountCodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiscountRepository extends CrudRepository<DiscountCodeEntity, String> {
    List<DiscountCodeEntity> findAll();
    DiscountCodeEntity findByCodeId(String codeId);
    boolean existsByCodeId(String codeId);
}

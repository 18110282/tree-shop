package com.treeshop.service;

import com.treeshop.entity.DiscountCodeEntity;

import java.util.List;

public interface DiscountCodeService {
    void saveDiscountCode(DiscountCodeEntity discountCodeEntity);

    DiscountCodeEntity findByCodeId(String codeId);

    List<DiscountCodeEntity> findAll();

    void deleteDiscountCode(String codeId);

    boolean checkCodeId(String codeId);

    String checkStatus(DiscountCodeEntity discountCodeEntity);
}

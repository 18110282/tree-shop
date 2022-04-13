package com.treeshop.service;

import com.treeshop.dao.DiscountRepository;
import com.treeshop.entity.DiscountCodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DiscountCodeService {
    @Autowired
    private DiscountRepository discountRepository;

    public void saveDiscoutCode(DiscountCodeEntity discountCodeEntity) {
        discountRepository.save(discountCodeEntity);
    }

    public DiscountCodeEntity findByCodeId(String codeId) {
        return discountRepository.findByCodeId(codeId);
    }

    public List<DiscountCodeEntity> findAll() {
        return discountRepository.findAll();
    }

    public void deleteDiscountCode(String codeId) {
        discountRepository.deleteById(codeId);
    }

    public boolean checkCodeId(String codeId) {
        return discountRepository.existsByCodeId(codeId);
    }

    public String checkStatus(DiscountCodeEntity discountCodeEntity) {
        Date currentDate = new Date();
        String status;
        boolean checkDate = currentDate.before(discountCodeEntity.getEndDate());
        if (checkDate) {
            status = "Còn hạng";
        } else {
            status = "Hết hạng";
        }
        return status;
    }

}

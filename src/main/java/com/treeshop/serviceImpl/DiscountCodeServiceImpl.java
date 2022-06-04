package com.treeshop.serviceImpl;

import com.treeshop.dao.DiscountRepository;
import com.treeshop.entity.DiscountCodeEntity;
import com.treeshop.service.DiscountCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DiscountCodeServiceImpl implements DiscountCodeService {
    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountCodeServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public void saveDiscountCode(DiscountCodeEntity discountCodeEntity) {
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

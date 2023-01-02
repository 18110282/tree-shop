package com.treeshop.service;

import com.treeshop.entity.ShipperEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShipperService {
    List<ShipperEntity> findAll();
    ShipperEntity findByShipperId(Integer shipperId);
    boolean checkUsername(String username);
    void saveShipper(ShipperEntity shipperEntity);
    void deleteShipper(Integer shipperId);
}

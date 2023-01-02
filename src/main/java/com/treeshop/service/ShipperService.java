package com.treeshop.service;

import com.treeshop.entity.OrdersEntity;
import com.treeshop.entity.ShipperEntity;
import com.treeshop.entity.StatusOfOrder;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShipperService {
    List<ShipperEntity> findAll();
    ShipperEntity findByShipperId(Integer shipperId);
    boolean checkUsername(String username);
    void saveShipper(ShipperEntity shipperEntity);
    void deleteShipper(Integer shipperId);
    boolean checkLogin(String username, String password);
    ShipperEntity findByUsername(String username);
    List<OrdersEntity> findListOrderOfShipper(Integer shipperId, StatusOfOrder status);
}

package com.treeshop.serviceImpl;

import com.treeshop.dao.OrdersRepository;
import com.treeshop.dao.ShipperRepository;
import com.treeshop.entity.OrdersEntity;
import com.treeshop.entity.ShipperEntity;
import com.treeshop.entity.StatusOfOrder;
import com.treeshop.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShipperServiceImpl implements ShipperService {
    private final ShipperRepository shipperRepository;
    private final OrdersRepository ordersRepository;

    @Autowired
    public ShipperServiceImpl(ShipperRepository shipperRepository, OrdersRepository ordersRepository) {
        this.shipperRepository = shipperRepository;
        this.ordersRepository = ordersRepository;
    }

    @Override
    public List<ShipperEntity> findAll() {
        return shipperRepository.findAllByEnabledIsTrue();
    }

    @Override
    public ShipperEntity findByShipperId(Integer shipperId) {
        return shipperRepository.findByShipperId(shipperId);
    }

    @Override
    public boolean checkUsername(String username) {
        return shipperRepository.existsByUsername(username);
    }

    @Override
    public void saveShipper(ShipperEntity shipperEntity) {
        shipperEntity.setEnabled(true);
        shipperRepository.save(shipperEntity);
    }

    @Override
    public void deleteShipper(Integer shipperId) {
        ShipperEntity shipperEntity = this.findByShipperId(shipperId);
        shipperEntity.setEnabled(false);
        shipperRepository.save(shipperEntity);
    }

    @Override
    public boolean checkLogin(String username, String password) {
        return shipperRepository.existsByUsernameAndPasswordAndEnabledIsTrue(username, password);
    }

    @Override
    public ShipperEntity findByUsername(String username) {
        return shipperRepository.findByUsername(username);
    }

    @Override
    public List<OrdersEntity> findListOrderOfShipper(Integer shipperId, StatusOfOrder status) {
        return ordersRepository.findByShipperIdAndStatus(shipperId, status);
    }
}

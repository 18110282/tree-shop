package com.treeshop.serviceImpl;

import com.treeshop.dao.ShipperRepository;
import com.treeshop.entity.ShipperEntity;
import com.treeshop.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShipperServiceImpl implements ShipperService {
    private final ShipperRepository shipperRepository;

    @Autowired
    public ShipperServiceImpl(ShipperRepository shipperRepository) {
        this.shipperRepository = shipperRepository;
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
}

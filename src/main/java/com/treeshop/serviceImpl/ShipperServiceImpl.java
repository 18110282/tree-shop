package com.treeshop.serviceImpl;

import com.treeshop.dao.ShipperRepository;
import com.treeshop.entity.ShipperEntity;
import com.treeshop.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipperServiceImpl implements ShipperService {
    private final ShipperRepository shipperRepository;

    @Autowired
    public ShipperServiceImpl(ShipperRepository shipperRepository) {
        this.shipperRepository = shipperRepository;
    }

    @Override
    public List<ShipperEntity> findAll() {
        return shipperRepository.findAll();
    }
}

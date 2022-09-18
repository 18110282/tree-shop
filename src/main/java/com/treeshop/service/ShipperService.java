package com.treeshop.service;

import com.treeshop.entity.ShipperEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShipperService {
    List<ShipperEntity> findAll();
}

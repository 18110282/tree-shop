package com.treeshop.dao;

import com.treeshop.entity.ShipperEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipperRepository extends CrudRepository<ShipperEntity, Integer> {
    List<ShipperEntity> findAll();
}

package com.treeshop.dao;

import com.treeshop.entity.ShipperEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipperRepository extends CrudRepository<ShipperEntity, Integer> {
    List<ShipperEntity> findAllByEnabledIsTrue();
    ShipperEntity findByShipperId(Integer shipperId);
    ShipperEntity findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByUsernameAndPasswordAndEnabledIsTrue(String username, String password);
}

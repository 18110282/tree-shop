package com.treeshop.dao;

import com.treeshop.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersEntity, String> {
    boolean existsByUsernameAndCodeId(String username, String codeId);
}

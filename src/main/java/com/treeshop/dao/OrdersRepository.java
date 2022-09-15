package com.treeshop.dao;

import com.treeshop.entity.OrdersEntity;
import com.treeshop.entity.StatusOfOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersEntity, String> {
    boolean existsByUsernameAndCodeId(String username, String codeId);
    OrdersEntity findByOrderId(String orderId);
    List<OrdersEntity> findByStatus(StatusOfOrder status);

    @Query("update OrdersEntity o set o.status = :status where o.orderId = :orderId")
    @Modifying
    void updateStatusOfOrder(@Param("status")StatusOfOrder status, @Param("orderId")String orderId);
}

package com.treeshop.dao;

import com.treeshop.entity.LineItemEntity;
import com.treeshop.entity.LineItemIdKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LineItemRepository extends CrudRepository<LineItemEntity, LineItemIdKey> {
    List<LineItemEntity> findByLineItemIdKey_OrderId(String orderId);
}

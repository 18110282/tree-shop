package com.treeshop.dao;

import com.treeshop.entity.LineItemEntity;
import com.treeshop.entity.LineItemIdKey;
import org.springframework.data.repository.CrudRepository;

public interface LineItemRepository extends CrudRepository<LineItemEntity, LineItemIdKey> {
    LineItemEntity findByLineItemIdKey(LineItemIdKey lineItemIdKey);
}

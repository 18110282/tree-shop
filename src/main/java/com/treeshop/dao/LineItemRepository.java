package com.treeshop.dao;

import com.treeshop.entity.lineitem.LineItemEntity;
import com.treeshop.entity.lineitem.LineItemIdKey;
import com.treeshop.entity.ProductsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineItemRepository extends CrudRepository<LineItemEntity, LineItemIdKey>, LineItemCustomRepository {
    List<LineItemEntity> findByLineItemIdKey_OrderId(String orderId);
    List<ProductsEntity> findTopSellProduct();
}

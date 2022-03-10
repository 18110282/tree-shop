package com.treeshop.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "lineitem")
public class LineItemEntity {
    @EmbeddedId
    private LineItemIdKey lineItemIdKey;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private OrdersEntity ordersEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductsEntity productsEntity;

}

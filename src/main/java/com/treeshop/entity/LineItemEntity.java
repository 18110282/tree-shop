package com.treeshop.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "lineitem")
public class LineItemEntity implements Serializable {
    @EmbeddedId
    private LineItemIdKey lineItemIdKey;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private Integer price;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private OrdersEntity ordersEntity;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductsEntity productsEntity;

    @Transient
    public Integer getTotalPerProduct() {
        return quantity * price;
    }

    @Transient
    public String getProductName(){
        return productsEntity.getProductName();
    }

}

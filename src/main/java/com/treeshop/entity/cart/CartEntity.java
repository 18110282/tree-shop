package com.treeshop.entity.cart;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="cart")
public class CartEntity {
    @EmbeddedId
    private CartIdKey cartIdKey;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_name", insertable = false, updatable = false)
    private UserEntity userEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductsEntity productsEntity;

    @Transient
    public Integer getTotalPerProduct() {
        return quantity * price;
    }

    @Transient
    public Integer getHeight(){
        return productsEntity.getHeight();
    }
    @Transient
    public Integer getLength(){
        return productsEntity.getLength();
    }
    @Transient
    public Integer getTotalWeightPerProduct(){
        return productsEntity.getWeight() * quantity;
    }
    @Transient
    public Integer getWidth(){
        return productsEntity.getWidth();
    }

}

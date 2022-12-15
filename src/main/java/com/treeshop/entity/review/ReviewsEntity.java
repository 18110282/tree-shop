package com.treeshop.entity.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.treeshop.entity.OrdersEntity;
import com.treeshop.entity.ProductsEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "reviews")
public class ReviewsEntity implements Serializable {

    @EmbeddedId
    private ReviewsIdKey reviewsIdKey;

    @Column(name = "content")
    private String content;

    @Column(name = "created")
    private Timestamp created;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductsEntity productsEntity;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private OrdersEntity ordersEntity;

    @Transient
    public String getProductName(){
        return productsEntity.getProductName();
    }

    @Transient
    public String getCategoryName() {
        return productsEntity.getCategoryEntity().getCategoryName();
    }

    @Transient
    public String getProductId() {
        return  productsEntity.getProductId();
    }

    @Transient
    public String getPhotosImagePath() {
        if (productsEntity.getImageUrl() == null || productsEntity.getProductId() == null) {
            return null;
        }
        return "/dynamic-resources/product-source/" + productsEntity.getProductId() + "/" + productsEntity.getImageUrl();
    }


}

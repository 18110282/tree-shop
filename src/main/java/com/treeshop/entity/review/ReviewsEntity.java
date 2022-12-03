package com.treeshop.entity.review;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductsEntity productsEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private OrdersEntity ordersEntity;


}

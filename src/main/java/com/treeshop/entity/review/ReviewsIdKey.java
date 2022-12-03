package com.treeshop.entity.review;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReviewsIdKey implements Serializable {
    @Column(name = "product_id", length = 20)
    private String productId;
    @Column(name = "order_id", length = 20)
    private String orderId;
}

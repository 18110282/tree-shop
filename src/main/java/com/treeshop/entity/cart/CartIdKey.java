package com.treeshop.entity.cart;

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
public class CartIdKey implements Serializable {
    @Column(name="user_name")
    private String username;

    @Column(name="product_id")
    private String productId;



}

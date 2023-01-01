package com.treeshop.entity.favouriteproduct;

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
public class FavouriteProductIdKey implements Serializable {
    @Column(name = "user_name", length = 20)
    private String username;
    @Column(name = "product_id", length = 20)
    private String productId;
}

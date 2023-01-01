package com.treeshop.entity.favouriteproduct;

import com.treeshop.entity.ProductsEntity;
import com.treeshop.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "favouriteproduct")
public class FavouriteProductEntity implements Serializable {
    @EmbeddedId
    private FavouriteProductIdKey favouriteProductIdKey;

    @Column(name = "created")
    private Timestamp created;

    @ManyToOne
    @JoinColumn(name = "user_name", insertable = false, updatable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductsEntity productsEntity;

    @Transient
    public String getProductName(){
        return productsEntity.getProductName();
    }

    @Transient
    public String getCategory(){
        return productsEntity.getCategoryEntity().getCategoryName();
    }

    @Transient
    public Integer getProductPrice(){
        return productsEntity.getDiscountPrice();
    }

    @Transient
    public String getPhotosImagePath(){
        return productsEntity.getPhotosImagePath();
    }
}

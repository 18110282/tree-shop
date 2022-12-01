package com.treeshop.entity;

import com.treeshop.entity.cart.CartEntity;
import com.treeshop.entity.lineitem.LineItemEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "products")
public class ProductsEntity {
    @Id
    @Column(name = "product_id", length = 10)
    private String productId;
    @Column(name = "product_name", length = 200)
    private String productName;
    @Column(name = "image_url", length = 300)
    private String imageUrl;
    @Column(name = "video_url", length = 300)
    private String videoUrl;
    @Column(name = "description")
    private String description;
    @Column(name = "unit_stock")
    private Integer unitInStock;
    @Column(name = "price")
    private Integer price;
    @Column(name = "create_date")
    private Timestamp createDate;
    @Column(name = "discount_percent")
    private Integer discountPercent;
    @Column(name = "category_id", length = 10)
    private String categoryId;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name="visit")
    private Integer visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @ToString.Exclude
    private CategoryEntity categoryEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productsEntity")
    @ToString.Exclude
    private List<LineItemEntity> lineItemEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productsEntity")
    @ToString.Exclude
    private List<CartEntity> cartEntityList;

    @Transient
    public String getPhotosImagePath() {
        if (imageUrl == null || productId == null) {
            return null;
        }
        return "/dynamic-resources/product-imgs/" + productId + "/" + imageUrl;
    }

    @Transient
    public Integer discountPrice;

    @Transient
    public Integer getDiscountPrice(){
        return price * (100 - discountPercent) / 100;
    }

}

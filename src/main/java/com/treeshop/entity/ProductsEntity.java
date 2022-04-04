package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "products")
public class ProductsEntity {
    @Id
    @Column(name = "product_id", length = 10)
    private String productId;
    @Column(name = "product_name", length = 200)
    private String productName;
    @Column(name = "image_url", length = 100)
    private String imageUrl;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity categoryEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productsEntity")
    private List<LineItemEntity> lineItemEntityList;

    @Transient
    public String getPhotosImagePath() {
        if (imageUrl == null || productId == null) {
            return null;
        }
        return "/dynamic-resources/product-imgs/" + productId + "/" + imageUrl;
    }
}

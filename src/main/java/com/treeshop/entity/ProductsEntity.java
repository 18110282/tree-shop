package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "products")
public class ProductsEntity {
    @Id
    @Column(name = "productId", length = 10)
    private String productId;

    @Column(name = "productName", length = 200)
    private String productName;

    @Column(name = "imageUrl", length = 100)
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "unitlnStock")
    private String unitlnStock;

    @Column(name = "price")
    private Integer price;

    @Column(name = "createDate")
    private Timestamp createDate;

    @Column(name = "discountPercent")
    private Integer discountPercent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    private CategoryEntity categoryEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productsEntity")
    private List<LineItemEntity> lineItemEntityList;

}

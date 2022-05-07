package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "category")
public class CategoryEntity {
    @Id
    @Column(name = "category_id", length = 10)
    private String categoryId;
    @Column(name = "category_name", length = 200)
    private String categoryName;
    @Column(name = "image_url", length = 300)
    private String imageUrl;
    @Column(name = "created_day")
    private Timestamp createDay;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryEntity")
    private List<ProductsEntity> productsEntityList;

    @Transient
    public String getPhotosImagePath() {
        if (imageUrl == null || categoryId == null) {
            return null;
        }
        return "/dynamic-resources/category-imgs/" + categoryId + "/" + imageUrl;
    }
}

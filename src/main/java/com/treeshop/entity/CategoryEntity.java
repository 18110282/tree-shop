package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "category")
public class CategoryEntity {
    @Id
    @Column(name = "categoryId", length = 10)
    private String categoryId;
    @Column(name = "categoryName", length = 200)
    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryEntity")
    private List<ProductsEntity> productsEntityList;
}

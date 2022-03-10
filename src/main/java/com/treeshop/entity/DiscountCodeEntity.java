package com.treeshop.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "discountcode")
public class DiscountCodeEntity {
    @Id
    @Column(name = "codeId", length = 10)
    private String codeId;
    @Column(name = "codeName", length = 200)
    private String codeName;
    @Column(name = "startDate")
    private Timestamp startDate;
    @Column(name = "endDate")
    private Timestamp endDate;
    @Column(name = "discountPercent")
    private Integer discountPercent;
    @Column(name = "status", length = 100)
    private String status;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "discountCodeEntity")
    private OrdersEntity ordersEntity;

}

package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class OrdersEntity {
    @Id
    @Column(name = "orderId", length = 10)
    private String orderId;
    @Column(name = "contactName", length = 100)
    private String contactName;
    @Column(name = "address", length = 200)
    private String address;
    @Column(name = "status", length = 100)
    private String status;
    @Column(name = "phoneNumber", length = 10)
    private String phoneNumber;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "orderDate")
    private String orderDate;
    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userName", insertable = false, updatable = false)
    private UserEntity userEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codeId")
    private DiscountCodeEntity discountCodeEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ordersEntity")
    private List<LineItemEntity> lineItemEntityList;
}

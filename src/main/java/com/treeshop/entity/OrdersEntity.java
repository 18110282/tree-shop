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
    @Column(name = "order_id", length = 10)
    private String orderId;
    @Column(name = "contact_name", length = 100)
    private String contactName;
    @Column(name = "address", length = 200)
    private String address;
    @Column(name = "status", length = 100)
    private String status;
    @Column(name = "phone_number", length = 10)
    private String phoneNumber;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "order_date")
    private String orderDate;
    @Column(name = "note")
    private String note;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "code_id")
    private String codeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_name", insertable = false, updatable = false)
    private UserEntity userEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id")
    private DiscountCodeEntity discountCodeEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ordersEntity")
    private List<LineItemEntity> lineItemEntityList;
}
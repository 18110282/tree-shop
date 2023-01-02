package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="shipper")
public class ShipperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shipperId;

    @Column(name = "username", length = 20, unique = true)
    private String username;

    @Column(name = "password", length = 20)
    private String password;

    @Column(name = "fullname", length = 100)
    private String fullname;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shipperEntity")
    private List<OrdersEntity> ordersEntityList;

}

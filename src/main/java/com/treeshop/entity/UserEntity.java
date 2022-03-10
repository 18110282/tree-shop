package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class UserEntity {
    @Id
    @Column(name = "userName", length = 20)
    private String userName;

    @Column(name = "passWord")
    private String passWord;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phoneNumber", unique = true)
    private String phoneNumber;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    private RoleEntity role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<OrdersEntity> ordersEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<PostsEntity> postsEntityList;

    @Override
    public String toString() {
        return "UserEntity{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status='" + status + '\'' +
                ", role=" + role +
                '}';
    }
}
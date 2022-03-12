package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class UserEntity implements Serializable {
    @Id
    @Column(name = "user_name", length = 20)
    private String userName;
    @Column(name = "pass_word", length = 20)
    private String passWord;
    @Column(name = "email", unique = true, length = 100)
    private String email;
    @Column(name = "phone_number", unique = true, length = 10)
    private String phoneNumber;
    @Column(name = "status", length = 100)
    private String status;
    @Column(name = "role_id", length = 10)
    private String role_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity roleEntity;

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
                ", role=" + roleEntity +
                '}';
    }
}
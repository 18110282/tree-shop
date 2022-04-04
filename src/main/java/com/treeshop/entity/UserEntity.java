package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity implements Serializable {
    @Id
    @Column(name = "user_name", length = 20)
    private String username;
    @Column(name = "pass_word", length = 20)
    private String password;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "phone_number", length = 10)
    private String phoneNumber;
    @Column(name = "status", length = 100)
    private String status;
    @Column(name = "role_id", length = 10)
    private String roleId;
    @Column(name = "full_name", length = 100)
    private String fullname;

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
                "userName='" + username + '\'' +
                ", passWord='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status='" + status + '\'' +
                ", role=" + roleEntity +
                '}';
    }
}
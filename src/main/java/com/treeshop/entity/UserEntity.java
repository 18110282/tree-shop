package com.treeshop.entity;

import com.treeshop.entity.cart.CartEntity;
import lombok.Getter;
import lombok.Setter;

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
    @Column(name = "role_id", length = 10)
    private String roleId;
    @Column(name = "full_name", length = 100)
    private String fullname;
    @Column(name = "verification_code", length = 100, updatable = false)
    private String verificationCode;
    @Column(name = "enabled")
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity roleEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<OrdersEntity> ordersEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<PostsEntity> postsEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<CartEntity> cartEntityList;

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roleId='" + roleId + '\'' +
                ", fullname='" + fullname + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
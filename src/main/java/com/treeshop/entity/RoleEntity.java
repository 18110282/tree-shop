package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "role")
public class RoleEntity {
    @Id
    @Column(name = "roleId", length = 10)
    private String id;

    @Column(name = "roleName", length = 100)
    private String roleName;

    @OneToMany(mappedBy = "role",  fetch = FetchType.LAZY)
    private List<UserEntity> userEntityList;

}
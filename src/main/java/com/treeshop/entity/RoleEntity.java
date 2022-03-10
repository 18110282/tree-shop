package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "role")
public class RoleEntity implements Serializable {
    @Id
    @Column(name = "role_id", length = 10)
    private String roleId;

    @Column(name = "role_name", length = 100)
    private String roleName;

    @OneToMany(mappedBy = "roleEntity", fetch = FetchType.LAZY)
    private List<UserEntity> userEntityList;

}
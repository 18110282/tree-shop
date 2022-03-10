package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "posts")
public class PostsEntity {
    @Id
    @Column(name = "postsId", length = 10)
    private String postsId;

    @Column(name = "title", length = 300)
    private String title;

    @Column(name = "descriptions")
    private String descriptions;

    @Column(name="headerImage", length = 100)
    private String headerImage;

    @Column(name = "createDate")
    private Timestamp createDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postsEntity")
    private List<ItemPostsListEntity> itemPostsListEntityList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userName", insertable = false, updatable = false)
    private UserEntity userEntity;
}

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
    @Column(name = "posts_id", length = 10)
    private String postsId;
    @Column(name = "title", length = 300)
    private String title;
    @Column(name = "descriptions")
    private String descriptions;
    @Column(name= "header_image", length = 100)
    private String headerImage;
    @Column(name = "create_date")
    private Timestamp createDate;
    @Column(name = "user_name", length = 20)
    private String username;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "introduce")
    private String introduce;

    @Transient
    private String author = "admin";

    @Transient
    public String getPhotosImageHeaderPath(){
        if (headerImage == null || postsId == null) {
            return null;
        }
        return "/dynamic-resources/post-imgs/" + postsId + "/" + headerImage;
    }
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postsEntity")
    private List<ItemPostsListEntity> itemPostsListEntityList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_name", insertable = false, updatable = false)
    private UserEntity userEntity;
}

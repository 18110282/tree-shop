package com.treeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "itempostslist")
public class ItemPostsListEntity {
    @Id
    @Column(name = "itemId", length = 10)
    private String itemId;
    @Column(name = "type", length = 100)
    private String type;
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postsId", insertable = false, updatable = false)
    private PostsEntity postsEntity;

}

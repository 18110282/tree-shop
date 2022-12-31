package com.treeshop.dao;

import com.treeshop.entity.PostsEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<PostsEntity, String> {
    List<PostsEntity> findAll();
    boolean existsByPostsId(String postsId);
    PostsEntity findByPostsId(String postsId);
}

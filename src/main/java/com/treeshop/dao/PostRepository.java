package com.treeshop.dao;

import com.treeshop.entity.PostsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<PostsEntity, String> {
    @Query("select p from PostsEntity  p where p.enabled = true")
    List<PostsEntity> findAllQuery();
    boolean existsByPostsId(String postsId);
    PostsEntity findByPostsId(String postsId);
    Page<PostsEntity> findAllByEnabledIsTrue(Pageable pageable);
    List<PostsEntity> findTop3ByEnabledIsTrueOrderByCreateDateDesc();
}

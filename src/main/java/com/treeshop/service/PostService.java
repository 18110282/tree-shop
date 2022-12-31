package com.treeshop.service;

import com.treeshop.entity.PostsEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    List<PostsEntity> listAll();
    boolean checkPostsId(String postsId);
    PostsEntity findByPostsId(String postsId);
    void savePost(PostsEntity postsEntity, MultipartFile multipartFileImg) throws IOException;
}

package com.treeshop.serviceImpl;

import com.treeshop.dao.PostRepository;
import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.PostsEntity;
import com.treeshop.service.CommonService;
import com.treeshop.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommonService commonService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, CommonService commonService) {
        this.postRepository = postRepository;
        this.commonService = commonService;
    }

    @Override
    public List<PostsEntity> listAll() {
        return postRepository.findAllByEnabledIsTrue();
    }

    @Override
    public boolean checkPostsId(String postsId) {
        return postRepository.existsByPostsId(postsId);
    }

    @Override
    public PostsEntity findByPostsId(String postsId) {
        return postRepository.findByPostsId(postsId);
    }

    @Override
    public void savePost(PostsEntity postsEntity, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String postsId = postsEntity.getPostsId();
        postsEntity.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        if (!fileName.equals("")) {
            String uploadDir = "./dynamic-resources/post-imgs/" + postsId;
            Path uploadPath = Paths.get(uploadDir);
            commonService.processFile(uploadPath, multipartFile, fileName);
        } else {
            PostsEntity postsEntity1 = this.findByPostsId(postsId);
            if (postsEntity1 != null) {
                fileName = postsEntity1.getHeaderImage();
            }
        }
        postsEntity.setHeaderImage(fileName);
        postsEntity.setEnabled(true);

        postRepository.save(postsEntity);
    }

    @Override
    public void deletePost(String postId) {
        PostsEntity postsEntity = postRepository.findByPostsId(postId);
        postsEntity.setEnabled(false);
        postRepository.save(postsEntity);
    }
}

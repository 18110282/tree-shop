package com.treeshop.dao;

import com.treeshop.entity.ProductsEntity;
import com.treeshop.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
    void deleteByUsername(String username);
    List<UserEntity> findAll();
    boolean existsByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
}

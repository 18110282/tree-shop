package com.treeshop.dao;

import com.treeshop.entity.ProductsEntity;
import com.treeshop.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
    void deleteByUsername(String username);
    List<UserEntity> findAll();
    boolean existsByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
}

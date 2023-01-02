package com.treeshop.dao;

import com.treeshop.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
    void deleteByUsername(String username);
    List<UserEntity> findAll();
    boolean existsByUsernameAndPassword(String username, String password);
//    boolean existsByUsernameAndPasswordIgnoreCase(String username, String password);
    boolean existsByUsername(String username);

    //Fìnd for client
    UserEntity findUserEntityByVerificationCode(String verificationCode);

    @Query("update UserEntity u set u.enabled = true where u.username = :username")
    @Modifying
    void setEnableTrue(@Param("username") String username);
}

package com.treeshop.service;

import com.treeshop.entity.UserEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UsersService {
    List<UserEntity> findAll();

    UserEntity findByUserName(String username);

    boolean checkLogin(String username, String password);

    void changePassword(UserEntity userEntity, String password);

    void saveUser(UserEntity userEntity);

    void saveUserInWeb(String username, UserEntity userEntity);

    void deleteUser(String username);

    boolean checkUsername(String username);

    void registerUser(UserEntity userEntity);

    void sendVerificationEmail(UserEntity userEntity, String siteURL) throws MessagingException, UnsupportedEncodingException;

    boolean verifyClient(String verificationCode);
}

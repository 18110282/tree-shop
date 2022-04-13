package com.treeshop.service;

import com.treeshop.dao.UserRepository;
import com.treeshop.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UsersService {
    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkLogin(String username, String password) {
        return userRepository.existsByUsernameAndPassword(username, password);
    }

    public UserEntity changePassword(UserEntity userEntity, String password) {
        userEntity.setPassword(password);
        return userRepository.save(userEntity);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}

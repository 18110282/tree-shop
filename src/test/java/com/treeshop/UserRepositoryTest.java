//package com.treeshop;
//
//import com.treeshop.dao.UserRepository;
//import com.treeshop.entity.UserEntity;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(false)
//public class UserRepositoryTest {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void insert(){
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserName("hieuxt");
//        userEntity.setPassWord("hieuxt");
//        userEntity.setEmail("@");
//        userEntity.setRole(1);
//        userRepository.save(userEntity);
//    }
//
//    @Test
//    public void findAll(){
//        //Iterable<UserEntity> userEntityIterable = userRepository.findAll();
//        //List<UserEntity> userEntityList = (List<UserEntity>) userEntityIterable;
//        List<UserEntity> userEntityList = userRepository.findAll();
//        //org.assertj.core.api.Assertions.assertThat(userEntityIterable).hasSizeGreaterThan(0);
//        for (UserEntity user:userEntityList) {
//            System.out.println(user);
//        }
//
//    }
//}

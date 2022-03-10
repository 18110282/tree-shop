package com.treeshop;

import com.treeshop.dao.UserRepository;
import com.treeshop.entity.RoleEntity;
import com.treeshop.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.Rollback;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void insert(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("hieuxt2tt1");
        userEntity.setPassWord("hieux2t1");
        userEntity.setEmail("@1t21");
        userEntity.setPhoneNumber("032t1121");
        userEntity.setStatus("active");
        userEntity.setRole_id("2");

//        RoleEntity roleEntity = new RoleEntity();
//        roleEntity.setRoleId("1");
//        userEntity.setRoleEntity(roleEntity);
        userRepository.save(userEntity);
    }

    @Test
    public void findAll(){
        Iterable<UserEntity> userEntityIterable = userRepository.findAll();
        //List<UserEntity> userEntityList = (List<UserEntity>) userEntityIterable;
//        userRepository.findAll();
        //org.assertj.core.api.Assertions.assertThat(userEntityIterable).hasSizeGreaterThan(0);
        for (UserEntity user:userEntityIterable) {
            System.out.println(user);
        }

    }
}

package com.treeshop;

import com.treeshop.dao.ProductsRepository;
import com.treeshop.dao.UserRepository;
import com.treeshop.entity.ProductsEntity;
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
import java.util.ListIterator;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductsRepository productsRepository;
    @Test
    public void insert(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("hieuxt1");
        userEntity.setPassword("hieuxt");
        userEntity.setEmail("@1t211");
        userEntity.setPhoneNumber("032t11121");
        userEntity.setStatus("active");
        userEntity.setRoleId("2");

//        RoleEntity roleEntity = new RoleEntity();
//        roleEntity.setRoleId("1");
//        userEntity.setRoleEntity(roleEntity);
        userRepository.save(userEntity);
    }

    @Test
    public void findAll(){
        //Iterable<UserEntity> userEntityIterable = userRepository.findAll();
        //List<UserEntity> userEntityList = (List<UserEntity>) userEntityIterable;
//        userRepository.findAll();
        //org.assertj.core.api.Assertions.assertThat(userEntityIterable).hasSizeGreaterThan(0);
        List<UserEntity> userEntityList = userRepository.findAll();
//        System.out.println(userEntityList);
        for (UserEntity user:userEntityList) {
            System.out.println(user);
        }
    }
    @Test
    public void findByUserName(){
        String username = "hieuxt";
        String password = "hieuxttttttt";
        UserEntity userEntity = userRepository.findByUsername(username);
        String a = userEntity.getRoleEntity().getRoleName();
    }

    @Test
    public void changePassword(){
        String username = "hieuxt";
        String password = "111";
        UserEntity userEntity = userRepository.findByUsername(username);
        userEntity.setPassword(password);
        userRepository.save(userEntity);
    }

    @Test
    public void addUser(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("hieua");
        userEntity.setPassword("hieua");
        userEntity.setFullname("hieua");
        userEntity.setEmail("hieua");
        userEntity.setPhoneNumber("hieua");
        userEntity.setStatus("active");
        userEntity.setRoleId("1");
        userRepository.save(userEntity);
    }

    @Test
    public void checkData(){
        boolean a = userRepository.existsByUsername("aaa");
        System.out.println(a);
    }

    @Test
    public void test(){
        List<ProductsEntity> userEntityList = productsRepository.findAll();
        String a = userEntityList.get(0).getCategoryEntity().getCategoryName();
    }

    @Test
    public void deleteUser(){
        userRepository.deleteById(",hieuxt1");
    }
}

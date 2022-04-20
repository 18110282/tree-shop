package com.treeshop;

import com.treeshop.dao.CartRepository;
import com.treeshop.dao.CategoryRepository;
import com.treeshop.dao.ProductsRepository;
import com.treeshop.dao.UserRepository;
import com.treeshop.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

//    @Test
//    public void test(){
//        List<ProductsEntity> userEntityList = productsRepository.findAll();
//        String a = userEntityList.get(0).getCategoryEntity().getCategoryName();
//    }

    @Test
    public void deleteUser(){
        userRepository.deleteById(",hieuxt1");
    }

    @Test
    public void listDiscountProduct(){
        List<ProductsEntity> productsEntity = productsRepository.findDiscountProduct();
    }

    //Cart
    @Test
    public void showCart(){
//        CartEntity cartEntity = new CartEntity();
//        CartIdKey cartIdKey = new CartIdKey();
//        cartIdKey.setProductId("2");
//        cartIdKey.setUserName("huy");
//        cartEntity.setCartIdKey(cartIdKey);
//        cartEntity.setQuantity(10);
//        cartRepository.save(cartEntity);
        cartRepository.deleteByCartIdKey_Username("hieu");
        //List<CartEntity> cartEntityList = cartRepository.findAll();
    }
    @Test
    public void checkExist(){
        String username = "hieuxt";
        String productId = "3";
        boolean c = cartRepository.existsByCartIdKey_Username(username);
    }

    @Test
    public void countSameUserAndProduct(){
        String username = "hieu";
        List<CartEntity> c = cartRepository.findCartEntitiesByCartIdKey_Username(username);

        CartEntity z = c.get(0);
        String a = z.getProductsEntity().getPhotosImagePath();
    }

    @Test
    public void updateQuantity(){
        cartRepository.updateQuantity(111, "hieu", "2");
    }

    @Test
    public void checkQuantity(){
        String username = "hieu";
        Integer quantity = 0;
        List<CartEntity> cartEntityList = cartRepository.getProductIdEmptyQuantity(username);
    }

    @Test
    public void listLatestProduct(){
        List<ProductsEntity> productsEntityList = productsRepository.findAllByOrderByCreateDateDesc();

        List<String> dayList = new ArrayList<>();
        for(int i = 0; i < productsEntityList.size(); i++){
            dayList.add(productsEntityList.get(i).getCreateDate().toString());
        }

    }

    @Test
    public void listProductsInCategory(){
        Pageable pageable = PageRequest.of(1,3);
        Page<ProductsEntity> productsEntityList = categoryRepository.findListProductInCategory("1", pageable);
        List<ProductsEntity> categoryEntityList = productsEntityList.getContent();

      //  List<ProductsEntity> productsEntityList1 = productsEntityList.getProductsEntityList();
    }

    @Test
    public void searchProduct(){
        String value = "Bông Vạn Thọ";
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProductsEntity> productsEntityList = productsRepository.searchProducts(value, pageable);
    }

    @Test
    public void findRandomListProductInSameCategory(){
        String categoryId = "1";
        List<ProductsEntity> productsEntityList = productsRepository.findRandomProductInSameCategory(categoryId);
    }

    @Test
    public void findMaxPrice(){
        Integer max = productsRepository.findMaxPrice();
    }

    @Test
    public void findListProductBetweenPrice(){
        Pageable page = PageRequest.of(1, 2);
        Page<ProductsEntity> productsEntityList = productsRepository.findAllByPriceBetween(1, 5000, page);
    }


}

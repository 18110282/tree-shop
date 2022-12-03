package com.treeshop.service;

import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.ProductsEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface ProductsService {
    List<ProductsEntity> findAllProductByQ();

    List<ProductsEntity> findRelatedProduct(String categoryId);

    void saveProductWithDiscountPercent(ProductsEntity productsEntity);

    void saveProduct(ProductsEntity productsEntity, MultipartFile multipartFileImg, MultipartFile multipartFileVideo) throws IOException;

    ProductsEntity findByProductId(String productId);

    ProductsEntity findByProductIdInWeb(String productId);

    boolean checkProductId(String productId);

    boolean checkProductIsDelete(String productId);

    void deleteProduct(String productId);

    List<CategoryEntity> findAllCategory();

    Page<ProductsEntity> findAll(Integer pageNumber);

    List<ProductsEntity> findListDiscountProduct();

    List<ProductsEntity> findListLatestProduct(Integer slide);

    Integer findDiscountPriceByProductId(String productId);

    Integer setDiscountPriceInDiscountList(ProductsEntity discountProduct);

    Integer findMaxPriceInAllProduct();

}

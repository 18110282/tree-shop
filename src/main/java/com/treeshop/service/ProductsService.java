package com.treeshop.service;

import com.treeshop.dao.CategoryRepository;
import com.treeshop.dao.ProductsRepository;
import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.DiscountCodeEntity;
import com.treeshop.entity.ProductsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProductsService {
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductsEntity> findAllProductByQ() {
        return productsRepository.findAllQuery();
    }

    public void saveProduct(ProductsEntity productsEntity) {
        productsRepository.save(productsEntity);
    }

    public void saveProductFile(ProductsEntity productsEntity, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        productsEntity.setCreateDate(timestamp);
        productsEntity.setImageUrl(fileName);
        productsEntity.setDiscountPercent(0);
        String productId = productsEntity.getProductId();
        if (!fileName.equals("")) {
//            String uploadDir = "./src/main/resources/static/product-imgs/" + savedProduct.getProductId();
            String uploadDir = "./dynamic-resources/product-imgs/" + productId;
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Could not save uploaded file: " + fileName);
            }
        }
        productsRepository.save(productsEntity);
    }

    public ProductsEntity findByProductId(String productId) {
        return productsRepository.findByProductId(productId);
    }

    public boolean checkProductId(String productId) {
        return productsRepository.existsByProductId(productId);
    }

    public void deleteProduct(String productId) {
        productsRepository.deleteById(productId);
    }

    public List<CategoryEntity> findAllCategory() {
        return categoryRepository.findAll();
    }

    public Page<ProductsEntity> findAll(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 6);
        return productsRepository.findAll(pageable);
    }

    public List<ProductsEntity> findListDiscountProduct() {
        return productsRepository.findDiscountProduct();
    }

    public Integer findDiscountPriceByProductId(String productId) {
        return productsRepository.findPriceByProductId(productId) * (100 - productsRepository.findDiscountPercentByProductId(productId)) / 100;
    }

    public Integer setDiscountPriceInDiscountList(ProductsEntity discountProduct){
        return discountProduct.getPrice() * (100 - discountProduct.getDiscountPercent()) / 100;
    }
}

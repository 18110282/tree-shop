package com.treeshop.serviceImpl;

import com.treeshop.dao.CategoryRepository;
import com.treeshop.dao.ProductsRepository;
import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.service.CommonService;
import com.treeshop.service.HomeService;
import com.treeshop.service.ProductsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProductServiceImpl implements ProductsService {
    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final HomeService homeService;
    private final CommonService commonService;

    public ProductServiceImpl(ProductsRepository productsRepository, CategoryRepository categoryRepository, HomeService homeService, CommonService commonService) {
        this.productsRepository = productsRepository;
        this.categoryRepository = categoryRepository;
        this.homeService = homeService;
        this.commonService = commonService;
    }

    public List<ProductsEntity> findAllProductByQ() {
        return productsRepository.findAllQuery();
    }

    public List<ProductsEntity> findRelatedProduct(String categoryId){
        return productsRepository.findRandomProductInSameCategory(categoryId);
    }
    public void saveProductWithDiscountPercent(ProductsEntity productsEntity) {
        productsRepository.save(productsEntity);
    }

    public void saveProduct(ProductsEntity productsEntity, MultipartFile multipartFileImg, MultipartFile multipartFileVideo) throws IOException {
        String fileNameImg = StringUtils.cleanPath(Objects.requireNonNull(multipartFileImg.getOriginalFilename()));
        String fileNameVideo = StringUtils.cleanPath(Objects.requireNonNull(multipartFileVideo.getOriginalFilename()));
        productsEntity.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        productsEntity.setEnabled(true);
        Integer visit = productsEntity.getVisit();
        Integer discountPercent = productsEntity.getDiscountPercent();

        if(discountPercent != null){
            productsEntity.setDiscountPercent(discountPercent);
        }
        else {
            productsEntity.setDiscountPercent(0);
        }
        if(visit != null){
            productsEntity.setVisit(visit);
        }
        else {
            productsEntity.setVisit(0);
        }

        String productId = productsEntity.getProductId();

        String uploadDir = "./dynamic-resources/product-source/" + productId;
        Path uploadPath = Paths.get(uploadDir);
        // For image
        if (!fileNameImg.equals("")) {
//            String uploadDir = "./src/main/resources/static/product-imgs/" + savedProduct.getProductId();
//            String uploadDir = "./dynamic-resources/product-source/" + productId;
//            Path uploadPath = Paths.get(uploadDir);
            commonService.processFile(uploadPath, multipartFileImg, fileNameImg);
        }
        else {
            ProductsEntity productsEntity1 = this.findByProductId(productId);
            if(productsEntity1 != null) {
                fileNameImg = productsEntity1.getImageUrl();
            }
        }
        productsEntity.setImageUrl(fileNameImg);

        // For video
        if (!fileNameVideo.equals("")) {
//            String uploadDir = "./src/main/resources/static/product-imgs/" + savedProduct.getProductId();
//            String uploadDir = "./dynamic-resources/product-source/" + productId;
//            Path uploadPath = Paths.get(uploadDir);
            commonService.processFile(uploadPath, multipartFileVideo, fileNameVideo);
        }
        else {
            ProductsEntity productsEntity1 = this.findByProductId(productId);
            if(productsEntity1 != null) {
                fileNameVideo = productsEntity1.getVideoUrl();
            }
        }

        productsEntity.setVideoUrl(fileNameVideo);

        productsRepository.save(productsEntity);
    }

    public ProductsEntity findByProductId(String productId) {
        return productsRepository.findByProductId(productId);
    }
    public ProductsEntity findByProductIdInWeb(String productId) {
        ProductsEntity productsEntity = productsRepository.findByProductId(productId);
        productsEntity.setVisit(productsEntity.getVisit()+1);
        productsRepository.save(productsEntity);
        return productsRepository.findByProductId(productId);
    }

    public boolean checkProductId(String productId) {
        return productsRepository.existsByProductId(productId);
    }

    @Override
    public boolean checkProductIsDelete(String productId) {
        // Enabled true means product is deleted
        return productsRepository.existsByProductIdAndEnabled(productId, false);
    }

    public void deleteProduct(String productId) {
        ProductsEntity productsEntity = this.findByProductId(productId);
        productsEntity.setEnabled(false);
        productsRepository.save(productsEntity);
    }

    public List<CategoryEntity> findAllCategory() {
        return categoryRepository.findAll();
    }

    public Page<ProductsEntity> findAll(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 9);
        return productsRepository.findAllByEnabledIsTrue(pageable);
    }

    public List<ProductsEntity> findListDiscountProduct() {
        List<ProductsEntity> listDiscountProduct = productsRepository.findDiscountProduct();
        for (ProductsEntity discountProduct : listDiscountProduct) {
            discountProduct.setDiscountPrice(this.setDiscountPriceInDiscountList(discountProduct));
        }
        return productsRepository.findDiscountProduct();
    }

    public List<ProductsEntity> findListLatestProduct(Integer slide){
        List<ProductsEntity> productsEntityList = productsRepository.findTop6ByEnabledIsTrueOrderByCreateDateDesc();
        return homeService.handleSlideOfTopSixProduct(slide, productsEntityList);
    }

    public Integer findDiscountPriceByProductId(String productId) {
        return productsRepository.findPriceByProductId(productId) * (100 - productsRepository.findDiscountPercentByProductId(productId)) / 100;
    }

    public Integer setDiscountPriceInDiscountList(ProductsEntity discountProduct){
        return discountProduct.getPrice() * (100 - discountProduct.getDiscountPercent()) / 100;
    }

    public Integer findMaxPriceInAllProduct(){
        return productsRepository.findMaxPrice();
    }

}

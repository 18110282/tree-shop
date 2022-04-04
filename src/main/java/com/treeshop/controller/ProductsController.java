package com.treeshop.controller;


import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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

@Controller
@RequestMapping(path = "/admin/products")
public class ProductsController {
    @Autowired
    ProductsService productsService;


    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        List<CategoryEntity> categoryEntityList = productsService.findAllCategory();
        model.addAttribute("product", new ProductsEntity());
        model.addAttribute("listCategory", categoryEntityList);
        model.addAttribute("titlePage", "Thêm sản phẩm");
        return "/views/admin/products/manageProducts";
    }

    @GetMapping("/list")
    public String showListProduct(Model model) {
        List<ProductsEntity> productsEntityList = productsService.findAll();
        model.addAttribute("listProduct", productsEntityList);
        return "/views/admin/products/listProduct";

    }

    @GetMapping("/edit/{productId}")
    public String showEditProductForm(@PathVariable("productId") String productId,
                                   Model model) {
        List<CategoryEntity> categoryEntityList = productsService.findAllCategory();
        ProductsEntity productsEntity = productsService.findByProductId(productId);
        model.addAttribute("listCategory", categoryEntityList);
        model.addAttribute("product", productsEntity);
        model.addAttribute("titlePage", "Chỉnh sửa sản phẩm: " + productId);
        model.addAttribute("edit", "");
        return "/views/admin/products/manageProducts";
    }
    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") String productId,
                                RedirectAttributes ra){
        productsService.deleteProduct(productId);
        ra.addFlashAttribute("successMessage", "Xóa sản phẩm: <strong> " + productId + "</strong> thành công.");
        return "redirect:/admin/products/list";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute(name = "product") ProductsEntity productsEntity,
                              @RequestParam(value = "fileImg", required = false) MultipartFile multipartFile,
                              HttpServletRequest request,
                              RedirectAttributes ra) throws IOException {
        //get previousUrl
        String previousUrl = request.getHeader("referer");
        String url = previousUrl.substring((request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()).length());
        //upload file
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
        if (url.equals("/admin/products/add-product")) {
            if (productsService.checkProductId(productId)) {
                ra.addFlashAttribute("errorMessage", productId);
                return "redirect:/admin/products/add-product";
            } else {
                ra.addFlashAttribute("successMessage", "Thêm sản phẩm: <strong> " + productId + "</strong> thành công.");
            }
        } else if (url.contains("/admin/products/edit")) {

                ra.addFlashAttribute("successMessage", "Chỉnh sửa sản phẩm: <strong> " + productId + "</strong> thành công.");
            }
        productsService.saveProduct(productsEntity);
        return "redirect:/admin/products/list";
    }
}

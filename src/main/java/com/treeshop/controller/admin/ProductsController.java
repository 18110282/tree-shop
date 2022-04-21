package com.treeshop.controller.admin;


import com.treeshop.controller.CommonController;
import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path = "/admin/products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    @Autowired
    private CommonController commonController;


    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        List<CategoryEntity> categoryEntityList = productsService.findAllCategory();
        model.addAttribute("product", new ProductsEntity());
        model.addAttribute("listCategory", categoryEntityList);
        model.addAttribute("titlePage", "Thêm sản phẩm");
        return "/views/admin/products/manage-products";
    }

    @GetMapping("/list")
    public String showListProduct(Model model) {
        List<ProductsEntity> productsEntityList = productsService.findAllProductByQ();
        model.addAttribute("listProduct", productsEntityList);
        return "/views/admin/products/list-product";

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
        return "/views/admin/products/manage-products";
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
        String previousUrl = commonController.getHeaderURL(request);
        String url = previousUrl.substring((request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()).length());
        //upload file
        String productId = productsEntity.getProductId();
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
        productsService.saveProductFile(productsEntity, multipartFile);
        return "redirect:/admin/products/list";
    }
}

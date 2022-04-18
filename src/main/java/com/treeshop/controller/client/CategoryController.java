package com.treeshop.controller.client;

import com.treeshop.controller.CommonController;
import com.treeshop.entity.CartEntity;
import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.entity.UserEntity;
import com.treeshop.service.CartService;
import com.treeshop.service.CategoryService;
import com.treeshop.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/home")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommonController commonController;

    @GetMapping("/list-web-product/category/{categoryId}")
    public String showListWebProductInCategory(@PathVariable(name = "categoryId") String categoryId,
                                               Model model,
                                               HttpSession session) {
        return showListWebProductInCategoryByPage(categoryId, model, session, 1);
    }

    @GetMapping("/list-web-product/category/{categoryId}/{page}")
    public String showListWebProductInCategoryByPage(@PathVariable(name = "categoryId") String categoryId, Model model, HttpSession session,
                                                     @PathVariable(name = "page") Integer currentPage) {
        Page<ProductsEntity> productsEntityPage = categoryService.findListProductInCategory(categoryId, currentPage);
        Integer totalPages = productsEntityPage.getTotalPages();
        commonController.setUpCommonAttributeOfListWebProduct(productsEntityPage, session, model);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPagesByCategory", totalPages);
        return "/views/client/list-web-product";
    }
}

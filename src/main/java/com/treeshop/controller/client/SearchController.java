package com.treeshop.controller.client;


import com.treeshop.controller.CommonController;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path = "/home")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private CommonController commonController;

    @GetMapping("/list-web-product/search/result")
    public String showListWebProductInCategory(@RequestParam(value = "keyword", required = false) String keyword,
                                               Model model,
                                               HttpSession session) {
        return showListWebProductInCategoryByPage(keyword, model, session, 1);
    }

    @GetMapping("/list-web-product/search/result/{keyword}/{page}")
    public String showListWebProductInCategoryByPage(@PathVariable(name = "keyword") String keyword, Model model, HttpSession session,
                                                     @PathVariable(name = "page") Integer currentPage) {
        Page<ProductsEntity> productsEntityPage = searchService.searchProduct(keyword, currentPage);
        List<ProductsEntity> listAllProductByValue = productsEntityPage.getContent();
        Long totalProducts = productsEntityPage.getTotalElements();
        Integer totalPages = productsEntityPage.getTotalPages();
        commonController.setUpCommonAttributeOfListWebProduct(session, model);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalPagesOfSearchFunction", totalPages);
        model.addAttribute("listProduct", listAllProductByValue);
        return "/views/client/list-web-product";
    }
}

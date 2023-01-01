package com.treeshop.controller.client;


import com.treeshop.controller.CommonController;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(path = "/home")
public class SearchController {
    private final SearchService searchService;
    private final CommonController commonController;

    @Autowired
    public SearchController(SearchService searchService, CommonController commonController) {
        this.searchService = searchService;
        this.commonController = commonController;
    }


    @GetMapping("/list-web-product/search/result")
    public String showListWebProductByKeyword(@RequestParam(value = "keyword", required = false) String keyword,
                                               Model model,
                                               HttpSession session) {
        return showListWebProductByKeywordByPage(keyword, model, session, 1);
    }

    @GetMapping("/list-web-product/search/result/{keyword}/{page}")
    public String showListWebProductByKeywordByPage(@PathVariable(name = "keyword") String keyword, Model model, HttpSession session,
                                                     @PathVariable(name = "page") Integer currentPage) {
        Page<ProductsEntity> productsEntityPage = searchService.searchProduct(keyword, currentPage);
        Integer totalPages = productsEntityPage.getTotalPages();
        commonController.setUpCommonAttributeOfListWebProduct(productsEntityPage.getContent(), productsEntityPage.getTotalElements() , session, model);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPagesOfSearchFunction", totalPages);
        return "/views/client/list-web-product";
    }

    @GetMapping("/list-web-product/search/price")
    public String showListWebProductByPrice(@RequestParam(value = "min") Integer min,
                                            @RequestParam(value = "max") Integer max,
                                            HttpSession session, Model model) {
        return showListWebProductByPriceByPage(min, max, model, session, 1);
    }

    @GetMapping("/list-web-product/search/price/{min}/{max}/{page}")
    public String showListWebProductByPriceByPage(@PathVariable(name = "min") Integer min,
                                                  @PathVariable(name = "max") Integer max,
                                                  Model model, HttpSession session,
                                                  @PathVariable(name = "page") Integer currentPage) {
        Page<ProductsEntity> productsEntityPage = searchService.searchProductByPrice(min, max, currentPage);
        Integer totalPages = productsEntityPage.getTotalPages();
        commonController.setUpCommonAttributeOfListWebProduct(productsEntityPage.getContent(), productsEntityPage.getTotalElements(), session, model);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPagesOfSearchByPrice", totalPages);
        return "/views/client/list-web-product";
    }

    @GetMapping("/list-web-product/search")
    public String showListWebProductByCondition(@RequestParam(value = "min", required = false) Integer min,
                                            @RequestParam(value = "max", required = false) Integer max,
                                            @RequestParam(value = "radio-weight", required = false) String weight,
                                            @RequestParam(value = "radio-height", required = false) String height,
                                            HttpSession session, Model model) {
        List<ProductsEntity> productsEntityList= searchService.searchProductByCondition(max, min, weight, height);
        commonController.setUpCommonAttributeOfListWebProduct(productsEntityList, (long) productsEntityList.size(), session, model);
        model.addAttribute("weight_check", weight);
        model.addAttribute("height_check", height);
        model.addAttribute("minP", min);
        model.addAttribute("maxP", max);
        return "/views/client/list-web-product";
    }


}

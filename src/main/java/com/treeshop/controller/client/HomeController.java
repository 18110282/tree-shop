package com.treeshop.controller.client;

import com.treeshop.controller.CommonController;
import com.treeshop.service.HomeService;
import com.treeshop.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    private final CommonController commonController;
    private final ProductsService productsService;
    private final HomeService homeService;

    @Autowired
    public HomeController(CommonController commonController, ProductsService productsService, HomeService homeService) {
        this.commonController = commonController;
        this.productsService = productsService;
        this.homeService = homeService;
    }

    @GetMapping(value={"/", "/home"})
    public String getIndex(HttpSession session,
                           Model model){
        model.addAttribute("numberProductInCart", commonController.getNumberProductInCart(session));
        model.addAttribute("categoryList", productsService.findAllCategory());
        model.addAttribute("topSellingProductList", homeService.findTopEightBestSellingProduct());
        model.addAttribute("topLatestProductList1", homeService.findTopSixLatestProduct(1));
        model.addAttribute("topLatestProductList2", homeService.findTopSixLatestProduct(2));
        model.addAttribute("topVisitProductList1", homeService.findTopSixVisitProduct(1));
        model.addAttribute("topVisitProductList2", homeService.findTopSixVisitProduct(2));
        model.addAttribute("topReviewProductList1", homeService.findTopSixReviewProduct(1));
        model.addAttribute("topReviewProductList2", homeService.findTopSixReviewProduct(2));
        model.addAttribute("home", "");
        return "/views/index";
    }

    @GetMapping("/home/contact")
    public String getContactInterface(HttpSession session,
                                      Model model){
        model.addAttribute("categoryList", productsService.findAllCategory());
        model.addAttribute("numberProductInCart", commonController.getNumberProductInCart(session));
        return "/views/client/contact";
    }
}

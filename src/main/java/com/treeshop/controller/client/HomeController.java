package com.treeshop.controller.client;

import com.treeshop.controller.CommonController;
import com.treeshop.service.HomeService;
import com.treeshop.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private CommonController commonController;

    @Autowired
    private ProductsService productsService;
    @Autowired
    private HomeService homeService;

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

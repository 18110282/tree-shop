package com.treeshop.controller.client;

import com.treeshop.controller.CommonController;
import com.treeshop.entity.UserEntity;
import com.treeshop.service.OrdersService;
import com.treeshop.service.ProductsService;
import com.treeshop.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/home")
public class InformationController {
    private final CommonController commonController;
    private final ProductsService productsService;
    private final UsersService usersService;
    private final OrdersService ordersService;

    @Autowired
    public InformationController(CommonController commonController, ProductsService productsService, UsersService usersService, OrdersService ordersService) {
        this.commonController = commonController;
        this.productsService = productsService;
        this.usersService = usersService;
        this.ordersService = ordersService;
    }

    @GetMapping("/{username}/information")
    public String viewHomeInformation(@PathVariable("username") String username,
                                      Model model,
                                      HttpSession session) {
        model.addAttribute("informationOfClient", usersService.findByUserName(username));
        model.addAttribute("listOrderOfClient", ordersService.findListOrderOfClient(username));
        model.addAttribute("listWaitConfirmOrderOfClient", ordersService.findListOrderOfClient(username));
        model.addAttribute("numberProductInCart", commonController.getNumberProductInCart(session));
        return "/views/client/information";
    }

    @PostMapping("/save/{username}/information")
    public String saveInformationOfClient(@PathVariable("username") String username,
                                          @ModelAttribute("informationOfClient")UserEntity client,
                                          RedirectAttributes ra) {
        usersService.saveUserInWeb(username, client);
        ra.addFlashAttribute("alert", "Lưu thông tin thành công");
        ra.addFlashAttribute("navItemHref", "#tabs-1");
        return "redirect:/home/" + username +"/information";
    }

//    @PostMapping("/save/{username}/password")
//    public String savePasswordOfClient(@PathVariable("username") String username,
//                                       @RequestParam(value = "oldPassword", required = false) String oldPassword,
//                                       @RequestParam(value = "newPassword", required = false) String newPassword,
//                                       RedirectAttributes ra){
//        if(usersService.savePasswordOfClient(username, oldPassword, newPassword)){
//            ra.addFlashAttribute("alert", "Thay đổi mật khẩu thành công");
//        }
//        else {
//            ra.addFlashAttribute("alert", "Mật khẩu cũ bạn nhập không đúng");
//        }
//        ra.addFlashAttribute("navItemHref", "#tabs-2");
//        return "redirect:/home/" + username +"/information";
//    }
}

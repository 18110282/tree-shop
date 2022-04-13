package com.treeshop.controller.client;


import com.treeshop.entity.CartEntity;
import com.treeshop.service.CartService;
import com.treeshop.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path="/home")
public class CheckoutController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CheckoutService checkoutService;

    @GetMapping("/{username}/cart/check-out")
    public String checkOut(@PathVariable(name="username") String username,
                           Model model,
                           RedirectAttributes ra,
                           HttpSession session){
        List<CartEntity> cartEntityList = cartService.findListCartByUsername(username);
        if(username.equals("no-user")){
            ra.addFlashAttribute("alert", "Mời bạn đăng nhập tài khoản để tiến hành đặt đơn");
            return "redirect:/home/{username}/cart/detail";
        }
        else {
            boolean checkUnitInStock = false;
            for (CartEntity cartEntity: cartEntityList) {
                if(cartEntity.getProductsEntity().getUnitInStock() == 0){
                    checkUnitInStock = true;
                }
            }
            if(checkUnitInStock){
                ra.addFlashAttribute("alert", "Có sản phẩm đã hết hàng, mời bạn xóa sản phẩm khỏi giỏ hàng hoặc chờ đợi hàng về, xin cảm ơn!");
                return "redirect:/home/{username}/cart/detail";
            }
            else if(cartService.checkQuantity(username, 0)){
                List<CartEntity> cartEntityEmptyQuantityList = cartService.findListCartByUsernameEmptyQuantity(username);
                String productId = cartEntityEmptyQuantityList.get(0).getProductsEntity().getProductName();
                ra.addFlashAttribute("alert", "Sản phẩm " + productId + " chưa điền số lượng (>0)");
                return "redirect:/home/{username}/cart/detail";
            }
        }
        Integer discountPercent = 0;
        Integer discount = (Integer) session.getAttribute("discountPercent");
        if(discount!= null){
            discountPercent = discount;
        }
        checkoutService.checkMoneyInCart(cartEntityList, discountPercent);
        Integer subTotal = checkoutService.getSubTotal();
        Integer total = checkoutService.getTotal();
        model.addAttribute("discountPercent", discountPercent);
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("total", total);
        model.addAttribute("cartDetailList", cartEntityList);
        model.addAttribute("numberProductInCart", cartEntityList.size());
        return "/views/client/check-out";
    }
}

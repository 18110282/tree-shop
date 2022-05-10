package com.treeshop.controller.client;


import com.treeshop.entity.cart.CartEntity;
import com.treeshop.service.CartService;
import com.treeshop.service.CheckoutService;
import com.treeshop.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/{username}/cart/check-out")
    public String checkOut(@PathVariable(name="username") String username,
                           Model model,
                           RedirectAttributes ra,
                           HttpSession session){
        List<CartEntity> cartEntityList = cartService.findListCartByUsername(username);
        Object client = session.getAttribute("client");
        String url = "redirect:/home/" + username + "/cart/detail";
        if(username.equals("no-user") || client == null ){
            ra.addFlashAttribute("alert", "Mời bạn đăng nhập tài khoản để tiến hành đặt đơn");
            return url;
        }
        else {
            boolean checkUnitInStock = false;
            for (CartEntity cartEntity: cartEntityList) {
                if (cartEntity.getProductsEntity().getUnitInStock() == 0) {
                    checkUnitInStock = true;
                    break;
                }
            }
            if(checkUnitInStock){
                ra.addFlashAttribute("alert", "Có sản phẩm đã hết hàng, mời bạn xóa sản phẩm khỏi giỏ hàng hoặc chờ hàng về trong thời gian tới, xin cảm ơn!");
                return url;
            }
            else if(cartService.checkQuantity(username, 0)){
                List<CartEntity> cartEntityEmptyQuantityList = cartService.findListCartByUsernameEmptyQuantity(username);
                String productName = cartEntityEmptyQuantityList.get(0).getProductsEntity().getProductName();
                ra.addFlashAttribute("alert", "Sản phẩm " + productName + " chưa điền số lượng (>0)");
                return url;
            }
            else if(cartService.compareQuantityInStockVsCart(cartService.findListCartByUsername(username)) != null){
                CartEntity cartEntity = cartService.compareQuantityInStockVsCart(cartService.findListCartByUsername(username));
                StringBuilder alert = new StringBuilder("Sản phẩm ");
                alert.append(cartEntity.getProductsEntity().getProductName());
                alert.append(" số lượng trong kho chỉ còn ");
                alert.append(cartEntity.getProductsEntity().getUnitInStock());
                alert.append(". \nMời bạn cập nhật lại để có thể tiến hành đặt đơn!");
                ra.addFlashAttribute("alert", alert);
                return url;

            }
        }
        if(!checkoutService.checkUserInCart(username)){
            ra.addFlashAttribute("alert", "Giỏ hàng trống, không thể đặt đơn!");
            return url;
        }
        else {
            Integer discountPercent = 0;
            Integer discount = (Integer) session.getAttribute("discountPercent");
            if (discount != null) {
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
        }
        return "/views/client/check-out";
    }

}

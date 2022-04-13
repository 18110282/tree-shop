package com.treeshop.controller.client;

import com.treeshop.controller.CommonController;
import com.treeshop.entity.CartEntity;
import com.treeshop.entity.DiscountCodeEntity;
import com.treeshop.service.CartService;
import com.treeshop.service.DiscountCodeService;
import com.treeshop.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path="/home")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    DiscountCodeService discountCodeService;
    @GetMapping("/{username}/cart/detail")
    public String viewDetailCart(@PathVariable(name="username") String username,
                                 Model model,
                                 HttpSession session){
        List<CartEntity> cartEntityList;
        int numberProductInCart = 0;
        if(username.equals("no-user"))
        {
            cartEntityList = (List<CartEntity>) session.getAttribute("cart");
            if(cartEntityList != null) {
                for (CartEntity cartEntity : cartEntityList) {
                    cartEntity.setProductsEntity(productsService.findByProductId(cartEntity.getCartIdKey().getProductId()));
                }
                numberProductInCart = cartEntityList.size();
            }
            else {
                model.addAttribute("a", "No product");
            }
        }
        else {
            cartEntityList = cartService.findListCartByUsername(username);
            numberProductInCart = cartEntityList.size();
        }
        model.addAttribute("numberProductInCart", numberProductInCart);
        model.addAttribute("cartDetailList", cartEntityList);
        return "/views/client/cart/detail-cart";
    }

    @GetMapping("/{username}/cart/delete/{productId}")
    public String deleteItemInCart(@PathVariable(name = "username") String username,
                                   @PathVariable(name="productId") String productId,
                                   HttpSession session,
                                   Model model){
        CommonController commonController = new CommonController();
        List<CartEntity> cartEntityList;
        if(username.equals("no-user")){
            cartEntityList = (List<CartEntity>) session.getAttribute("cart");
            if(cartEntityList != null) {
                int index = commonController.exists(productId, cartEntityList);
                if(index != -1){
                    cartEntityList.remove(index);
                }
            }
            else {
                model.addAttribute("a", "No product");
            }
        }
        else {
            cartService.deleteItem(username,productId);
        }
        return "redirect:/home/{username}/cart/detail";
    }

    @PostMapping("/{username}/cart/detail/check-discount-code")
    public String checkDisCountCode(@RequestParam(value = "discountCode", required = false) String discountCode,
                                    @PathVariable(name = "username") String username,
                                    HttpSession session,
                                    RedirectAttributes ra){
        boolean exist = discountCodeService.checkCodeId(discountCode);
        if(exist){
            DiscountCodeEntity discountCodeEntity = discountCodeService.findByCodeId(discountCode);
            String status = discountCodeEntity.getStatus();
            if(status.equals("Còn hạng")){
                session.setAttribute("discountPercent", discountCodeEntity.getDiscountPercent());
                session.setAttribute("discountCode", discountCodeEntity);
                ra.addFlashAttribute("alert", "Áp dụng mã thành công");
            }
            else {
                ra.addFlashAttribute("alert", "Mã đã hết hạng, hãy nhập mã khác");
            }
        }
        else {
            ra.addFlashAttribute("alert", "Mã không tồn tại, hãy nhập lại");
        }
        return "redirect:/home/{username}/cart/detail";
    }

    @GetMapping("/{username}/cart/detail/delete-discount-code")
    public String deleteDiscountCode(@PathVariable(name = "username") String username,
                                     HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("discountPercent");
        session.removeAttribute("discountCode");
        return "redirect:/home/{username}/cart/detail";
    }
}

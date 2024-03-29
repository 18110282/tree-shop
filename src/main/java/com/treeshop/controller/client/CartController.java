package com.treeshop.controller.client;

import com.treeshop.controller.CommonController;
import com.treeshop.entity.cart.CartEntity;
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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(path = "/home")
public class CartController {
    private final CartService cartService;

    private final ProductsService productsService;

    private final DiscountCodeService discountCodeService;

    @Autowired
    public CartController(CartService cartService, ProductsService productsService, DiscountCodeService discountCodeService) {
        this.cartService = cartService;
        this.productsService = productsService;
        this.discountCodeService = discountCodeService;
    }

    @GetMapping("/{username}/cart/detail")
    public String viewDetailCart(@PathVariable(name = "username") String username,
                                 Model model,
                                 HttpSession session) {
        List<CartEntity> cartEntityList;
        int numberProductInCart = 0;
        if (username.equals("no-user")) {
            cartEntityList = (List<CartEntity>) session.getAttribute("cart");
            if (cartEntityList != null) {
                for (CartEntity cartEntity : cartEntityList) {
                    cartEntity.setProductsEntity(productsService.findByProductId(cartEntity.getCartIdKey().getProductId()));
                }
                numberProductInCart = cartEntityList.size();
            } else {
                model.addAttribute("a", "No product");
            }
        } else {
            boolean checkUser = cartService.checkExistUser(username);
            if (!checkUser) {
                if (model.asMap().get("delete") == null) {
                    cartEntityList = (List<CartEntity>) session.getAttribute("cart");
                    if (cartEntityList != null) {
                        cartService.saveCartFromSessionToCartEntity(cartEntityList, username);
                        session.removeAttribute("cart");
                    }
                }
            }
            cartEntityList = cartService.findListCartByUsername(username);
            numberProductInCart = cartEntityList.size();
        }
        model.addAttribute("numberProductInCart", numberProductInCart);
        model.addAttribute("cartDetailList", cartEntityList);
        model.addAttribute("categoryList", productsService.findAllCategory());
        return "views/client/cart/cart-detail";
    }

    @GetMapping("/{username}/cart/delete/{productId}")
    public String deleteItemInCart(@PathVariable(name = "username") String username,
                                   @PathVariable(name = "productId") String productId,
                                   HttpSession session,
                                   RedirectAttributes ra) {
        CommonController commonController = new CommonController();
        List<CartEntity> cartEntityList;
        if (username.equals("no-user")) {
            cartEntityList = (List<CartEntity>) session.getAttribute("cart");
            if (cartEntityList != null) {
                int index = commonController.exists(productId, cartEntityList);
                if (index != -1) {
                    cartEntityList.remove(index);
                }
            }
        } else {
            cartService.deleteItem(username, productId);
            session.removeAttribute("cart");
        }
        ra.addFlashAttribute("delete", "yes");
        return "redirect:/home/" + username + "/cart/detail";
    }

    @PostMapping("/{username}/cart/detail/check-discount-code")
    public String checkDisCountCode(@RequestParam(value = "discountCode", required = false) String discountCode,
                                    @PathVariable(name = "username") String username,
                                    HttpSession session,
                                    RedirectAttributes ra) {
        boolean exist = discountCodeService.checkCodeId(discountCode);
        if (exist) {
            DiscountCodeEntity discountCodeEntity = discountCodeService.findByCodeId(discountCode);
            String status = discountCodeEntity.getStatus();
            if (status.equals("Còn hạng")) {
                if (cartService.checkUsedCodeIdOfUser(username, discountCode)) {
                    ra.addFlashAttribute("alert", "Bạn đã sử dụng mã này rồi ^^ Mời bạn nhập mã khác!");
                } else {
                    if (Date.valueOf(LocalDate.now()).before(discountCodeEntity.getStartDate())) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        ra.addFlashAttribute("alert", "Mã " + discountCode + " chỉ áp dụng từ ngày " + dateFormat.format(discountCodeEntity.getStartDate()));
                    } else {
                        session.setAttribute("discountPercent", discountCodeEntity.getDiscountPercent());
                        session.setAttribute("discountCode", discountCodeEntity);
                        ra.addFlashAttribute("alert", "Áp dụng mã thành công");
                    }
                }
            } else {
                ra.addFlashAttribute("alert", "Mã đã hết hạng, hãy nhập mã khác");
            }
        } else {
            ra.addFlashAttribute("alert", "Mã không tồn tại, hãy nhập lại");
        }
        return "redirect:/home/" + username + "/cart/detail";
    }

    @GetMapping("/{username}/cart/detail/delete-discount-code")
    public String deleteDiscountCode(@PathVariable(name = "username") String username,
                                     HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("discountPercent");
        session.removeAttribute("discountCode");
        return "redirect:/home/" + username + "/cart/detail";
    }
}

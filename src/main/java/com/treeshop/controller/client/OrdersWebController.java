package com.treeshop.controller.client;


import com.paypal.base.rest.PayPalRESTException;
import com.treeshop.entity.lineitem.LineItemEntity;
import com.treeshop.entity.OrdersEntity;
import com.treeshop.service.OrdersService;
import com.treeshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/home")
public class OrdersWebController {
    private final OrdersService ordersService;
    private final PaymentService paymentService;

    @Autowired
    public OrdersWebController(OrdersService ordersService, PaymentService paymentService) {
        this.ordersService = ordersService;
        this.paymentService = paymentService;
    }

    @PostMapping("/{username}/create-order")
    public String createOrder(@ModelAttribute("order") OrdersEntity ordersEntity,
                              @PathVariable(name = "username") String username,
                              HttpSession session, HttpServletRequest request, RedirectAttributes ra) throws MessagingException, IOException, PayPalRESTException {
        String payment = ordersEntity.getPayment();
        String url;
        if(payment.equals("cod")){
            ordersService.addOrder(ordersEntity, username, session);
            ordersService.sendMailInvoice(ordersEntity.getOrderId(), ordersEntity.getEmail(), request);
            ra.addFlashAttribute("orderSuccess", "Đặt hàng thành công");
            url = "/home/list-web-product";
        }
        else if(payment.equals("paypal")){
            url = paymentService.authorizePayment(ordersEntity, request);
            session.setAttribute("orderSessionForPayPal", ordersEntity);
        }
        else {
            url = "/error";
        }
        return "redirect:" + url;

    }

    @GetMapping("/{username}/paypal")
    public String payByPaypal(@PathVariable(name = "username") String username,
                            @Param("paymentId") String paymentId,
                            @Param("PayerID") String PayerID,
                            HttpSession session,
                            HttpServletRequest request,
                            RedirectAttributes ra) throws PayPalRESTException, MessagingException, IOException {
        OrdersEntity ordersEntity = (OrdersEntity) session.getAttribute("orderSessionForPayPal");
        paymentService.executePayment(paymentId, PayerID, ordersEntity, username, session, request);
        ra.addFlashAttribute("orderSuccess", "Đặt hàng thành công");
        return "redirect:/home/list-web-product";
    }

    @GetMapping("/send-invoice/order/{orderId}")
    public String sendOrderDetailToMail(@PathVariable(name = "orderId") String orderId,
                                        Model model, HttpServletRequest request) {
        OrdersEntity ordersEntity = ordersService.findOrderEntityById(orderId);
        List<LineItemEntity> lineItemEntityList = ordersService.findLineItemOfOrderId(orderId);
        if (ordersEntity.getDiscountCodeEntity() != null) {
            model.addAttribute("discountPercent", ordersEntity.getDiscountCodeEntity().getDiscountPercent());
        }
        model.addAttribute("subTotal", ordersService.getSubToTalOfOrder(orderId));
        model.addAttribute("order", ordersEntity);
        model.addAttribute("lineItemList", lineItemEntityList);
        model.addAttribute("url", request.getRequestURL());
        return "/views/client/mail-invoice-detail";
    }
}

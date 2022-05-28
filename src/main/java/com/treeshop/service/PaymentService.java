package com.treeshop.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.treeshop.entity.OrdersEntity;
import com.treeshop.entity.cart.CartEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    @Value("${paypalProperties.clientID}")
    private String CLIENT_ID;
    @Value("${paypalProperties.clientSecret}")
    private String CLIENT_SECRET;
    @Value("${paypalProperties.mode}")
    private String MODE;

    @Autowired
    private CommonService commonService;
    @Autowired
    private OrdersService ordersService;
//    @Autowired
//    private CartService cartService;

    private RedirectUrls getRedirectUrls(HttpServletRequest request, OrdersEntity ordersEntity) {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(request.getHeader("referer"));
        redirectUrls.setReturnUrl(commonService.getSiteUrl(request) + "/home/" + ordersEntity.getUsername() + "/paypal");
        return redirectUrls;
    }

    private Payer getPayerInformation(OrdersEntity ordersEntity) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(ordersEntity.getContactName())
                .setEmail(ordersEntity.getEmail());
        payer.setPayerInfo(payerInfo);
        return payer;
    }

    private List<Transaction> getTransactionInformation(OrdersEntity ordersEntity) {
        Details details = new Details();
//        details.setSubtotal("90.00");
        Amount amount = new Amount();
        Float total = ordersEntity.getTotalPrice().floatValue() / 23100;
        DecimalFormat df = new DecimalFormat("0.00");
        amount.setCurrency("USD");
        amount.setTotal(df.format(total));
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);

//        List<CartEntity> cartEntityList = cartService.findListCartByUsername(ordersEntity.getUsername());
//        ItemList itemList = new ItemList();
//        List<Item> items = new ArrayList<>();
//        for (CartEntity cartEntity: cartEntityList) {
//            Item item = new Item();
//            item.setName(cartEntity.getProductsEntity().getProductName());
//            Float price = cartEntity.getPrice().floatValue() / 23100;
//            item.setPrice(df.format(price));
//            item.setCurrency("USD");
//            item.setQuantity(cartEntity.getQuantity().toString());
//            items.add(item);
//        }
//        itemList.setItems(items);
//        transaction.setItemList(itemList);
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        return transactionList;
    }

    private String getApprovalLink(Payment approvedPayment){
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;
        for (Links link: links ){
            if(link.getRel().equalsIgnoreCase("approval_url")){
                approvalLink = link.getHref();
                break;
            }
        }
        return approvalLink;
    }

    public String authorizePayment(OrdersEntity ordersEntity, HttpServletRequest request) throws PayPalRESTException {
        Payer payer = this.getPayerInformation(ordersEntity);
        RedirectUrls redirectUrls = this.getRedirectUrls(request, ordersEntity);
        List<Transaction> transactionList = this.getTransactionInformation(ordersEntity);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(transactionList)
                .setRedirectUrls(redirectUrls)
                .setPayer(payer)
                .setIntent("authorize");

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        Payment approvedPayment = requestPayment.create(apiContext);
        System.out.println(approvedPayment);
        return this.getApprovalLink(approvedPayment);
    }

    public void executePayment(String paymentId, String payerId, OrdersEntity ordersEntity, String username, HttpSession session, HttpServletRequest request)
            throws PayPalRESTException, MessagingException, IOException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        payment.execute(apiContext, paymentExecution);
        ordersService.addOrder(ordersEntity, username, session);
        ordersService.sendMailInvoice(ordersEntity.getOrderId(), ordersEntity.getEmail(), request);
        session.removeAttribute("orderSessionForPayPal");
    }
}

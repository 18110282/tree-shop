package com.treeshop.serviceImpl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.treeshop.entity.OrdersEntity;
import com.treeshop.service.CommonService;
import com.treeshop.service.OrdersService;
import com.treeshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Value("${paypalProperties.clientID}")
    private String CLIENT_ID;
    @Value("${paypalProperties.clientSecret}")
    private String CLIENT_SECRET;
    @Value("${paypalProperties.mode}")
    private String MODE;

    private final CommonService commonService;
    private final OrdersService ordersService;

    @Autowired
    public PaymentServiceImpl(CommonService commonService, OrdersService ordersService) {
        this.commonService = commonService;
        this.ordersService = ordersService;
    }

/*
    @Autowired
    private CartService cartService;
*/

    public RedirectUrls getRedirectUrls(HttpServletRequest request, OrdersEntity ordersEntity) {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(request.getHeader("referer"));
        redirectUrls.setReturnUrl(commonService.getSiteUrl(request) + "/home/" + ordersEntity.getUsername() + "/paypal");
        return redirectUrls;
    }

    public Payer getPayerInformation(OrdersEntity ordersEntity) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(ordersEntity.getContactName())
                .setEmail(ordersEntity.getEmail());
        payer.setPayerInfo(payerInfo);
        return payer;
    }

    public List<Transaction> getTransactionInformation(OrdersEntity ordersEntity) throws IOException {
        Details details = new Details();
//        details.setSubtotal("90.00");
        // URL
        String URL = "https://v6.exchangerate-api.com/v6/cdd8f9308843227942ec4fd7/latest/USD";
        // Making request to get exchangerate
        java.net.URL url = new URL(URL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        // Using json to read result
        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonObject = root.getAsJsonObject();
        // Get conversion_rates from VND to USD
        float conversion_rate = jsonObject.getAsJsonObject("conversion_rates").get("VND").getAsFloat();

        Amount amount = new Amount();
        Float total = ordersEntity.getTotalPrice().floatValue() / conversion_rate;

        DecimalFormat df = new DecimalFormat("0.00");
        amount.setCurrency("USD");
        amount.setTotal(df.format(total));
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);

/*
        List<CartEntity> cartEntityList = cartService.findListCartByUsername(ordersEntity.getUsername());
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();
        for (CartEntity cartEntity: cartEntityList) {
            Item item = new Item();
            item.setName(cartEntity.getProductsEntity().getProductName());
            Float price = cartEntity.getPrice().floatValue() / 23100;
            item.setPrice(df.format(price));
            item.setCurrency("USD");
            item.setQuantity(cartEntity.getQuantity().toString());
            items.add(item);
        }
        itemList.setItems(items);
        transaction.setItemList(itemList);
*/
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        return transactionList;
    }

    public String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;
        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }
        return approvalLink;
    }

    public String authorizePayment(OrdersEntity ordersEntity, HttpServletRequest request) throws PayPalRESTException, IOException {
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
        session.removeAttribute("cart");
    }
}

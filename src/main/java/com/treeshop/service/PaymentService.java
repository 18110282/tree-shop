package com.treeshop.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.treeshop.entity.OrdersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public interface PaymentService {
    RedirectUrls getRedirectUrls(HttpServletRequest request, OrdersEntity ordersEntity);

    Payer getPayerInformation(OrdersEntity ordersEntity);

    List<Transaction> getTransactionInformation(OrdersEntity ordersEntity) throws IOException;

    String getApprovalLink(Payment approvedPayment);

    String authorizePayment(OrdersEntity ordersEntity, HttpServletRequest request) throws PayPalRESTException, IOException;

    void executePayment(String paymentId, String payerId, OrdersEntity ordersEntity, String username, HttpSession session, HttpServletRequest request)
            throws PayPalRESTException, MessagingException, IOException;
}

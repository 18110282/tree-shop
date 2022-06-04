package com.treeshop.serviceImpl;

import com.treeshop.dao.CartRepository;
import com.treeshop.dao.LineItemRepository;
import com.treeshop.dao.OrdersRepository;
import com.treeshop.entity.DiscountCodeEntity;
import com.treeshop.entity.OrdersEntity;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.entity.cart.CartEntity;
import com.treeshop.entity.lineitem.LineItemEntity;
import com.treeshop.entity.lineitem.LineItemIdKey;
import com.treeshop.service.CommonService;
import com.treeshop.service.OrdersService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {
    private final OrdersRepository ordersRepository;
    private final LineItemRepository lineItemRepository;
    private final CartRepository cartRepository;
    private final CommonService commonService;
    private final JavaMailSender javaMailSender;

    @Autowired
    public OrdersServiceImpl(OrdersRepository ordersRepository, LineItemRepository lineItemRepository, CartRepository cartRepository, CommonService commonService, JavaMailSender javaMailSender) {
        this.ordersRepository = ordersRepository;
        this.lineItemRepository = lineItemRepository;
        this.cartRepository = cartRepository;
        this.commonService = commonService;
        this.javaMailSender = javaMailSender;
    }

    public List<OrdersEntity> findAll(){
        return ordersRepository.findAll();
    }
    public OrdersEntity findOrderEntityById(String orderId){
        return ordersRepository.findByOrderId(orderId);
    }

    public List<LineItemEntity> findLineItemOfOrderId(String orderId){
        return lineItemRepository.findByLineItemIdKey_OrderId(orderId);
    }

    public void addOrder(OrdersEntity ordersEntity, String username, HttpSession session) throws MessagingException, IOException {
        List<CartEntity> cartEntityList = cartRepository.findCartEntitiesByCartIdKey_Username(username);
        DiscountCodeEntity discountCode = (DiscountCodeEntity) session.getAttribute("discountCode");
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        if (discountCode != null) {
            ordersEntity.setCodeId(discountCode.getCodeId());
        }
        ordersEntity.setOrderId(this.createOrderId());
        ordersEntity.setOrderDate(timestamp.toString());
        //add orderEntity
        ordersRepository.save(ordersEntity);
        //add line-item from cart of user
        for (CartEntity cartEntity : cartEntityList) {
            LineItemEntity lineItemEntity = new LineItemEntity();
            LineItemIdKey linetItemIdKey = new LineItemIdKey();
            linetItemIdKey.setOrderId(ordersEntity.getOrderId());
            linetItemIdKey.setProductId(cartEntity.getCartIdKey().getProductId());
            lineItemEntity.setLineItemIdKey(linetItemIdKey);
            lineItemEntity.setQuantity(cartEntity.getQuantity());
            lineItemEntity.setPrice(cartEntity.getPrice());
            //minus quantity in stock
            ProductsEntity productsEntity = cartEntity.getProductsEntity();
            productsEntity.setUnitInStock(productsEntity.getUnitInStock() - cartEntity.getQuantity());
            lineItemRepository.save(lineItemEntity);
        }
        //remove discountCode session
        session.removeAttribute("discountCode");
        session.removeAttribute("discountPercent");
        //delete cart when add line-item success
        cartRepository.deleteByCartIdKey_Username(username);
    }

    //random orderId
    public String createOrderId() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        //Get date-month-year to generate orderId
        String orderId = timestamp.toString().substring(0, 10);
        orderId = orderId.replace("-", "");
        orderId = orderId + RandomString.make(5);

        return orderId;
    }

    //copy source HTML code from Processed Invoice Web Page
    public String copySourceHTMLCodeFromWebPage(URL url) throws IOException {
        URLConnection urlCon = url.openConnection();
        BufferedReader in;

        if (urlCon.getHeaderField("Content-Encoding") != null
                && urlCon.getHeaderField("Content-Encoding").equals("gzip")) {
            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(
                    urlCon.getInputStream())));
        } else {
            in = new BufferedReader(new InputStreamReader(
                    urlCon.getInputStream()));
        }

        String inputLine;
        StringBuilder sb = new StringBuilder();
        //copy each line to sb
        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        in.close();
        return sb.toString();
    }

    //Send Mail Invoice from processed mail-invoice-detail.html
    public void sendMailInvoice(String orderId, String email, HttpServletRequest request) throws MessagingException, IOException {
        String subject = "Chi tiết đơn hàng #" + orderId;
        String senderName = "Tree Shop";
        //Get content of HTML source
        URL url = new URL(commonService.getSiteUrl(request) + "/home/send-invoice/order/" + orderId);
        String content = this.copySourceHTMLCodeFromWebPage(url);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        mimeMessageHelper.setFrom("treeshop49@gmail.com", senderName);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        //Send HTML to content of mail
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(message);
    }

    public Integer getSubToTalOfOrder(String orderId){
        List<LineItemEntity> lineItemEntityList = lineItemRepository.findByLineItemIdKey_OrderId(orderId);
        int subTotal = 0;
        for (LineItemEntity lineItemEntity :lineItemEntityList){
            subTotal = subTotal + lineItemEntity.getTotalPerProduct();
        }
        return subTotal;
    }
    public void setTransientOfOrder(String orderId){
        OrdersEntity ordersEntity = this.findOrderEntityById(orderId);
        ordersEntity.setSubTotalPrice(this.getSubToTalOfOrder(orderId));
        if(ordersEntity.getDiscountCodeEntity() != null) {
            ordersEntity.setDiscountPercent(ordersEntity.getDiscountCodeEntity().getDiscountPercent());
        }
        else {
            ordersEntity.setDiscountPercent(0);
        }
        ordersRepository.save(ordersEntity);
    }
}

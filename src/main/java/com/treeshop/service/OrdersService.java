package com.treeshop.service;

import com.treeshop.dao.CartRepository;
import com.treeshop.dao.LineItemRepository;
import com.treeshop.dao.OrdersRepository;
import com.treeshop.dao.ProductsRepository;
import com.treeshop.entity.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private LineItemRepository lineItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CheckoutService checkoutService;

    public void addOrder(OrdersEntity ordersEntity, String username, HttpSession session) {
        List<CartEntity> cartEntityList = cartRepository.findCartEntitiesByCartIdKey_Username(username);
        DiscountCodeEntity discountCode = (DiscountCodeEntity) session.getAttribute("discountCode");
        Integer discountPercent = 0;
        if (discountCode != null) {
            discountPercent = discountCode.getDiscountPercent();
            ordersEntity.setCodeId(discountCode.getCodeId());
        }
        checkoutService.checkMoneyInCart(cartEntityList, discountPercent);
        Integer total = checkoutService.getTotal();
        ordersEntity.setOrderId(this.createOrderId());
        ordersEntity.setTotalPrice(total);
        //add orderEntity
        ordersRepository.save(ordersEntity);
        //add line-item from cart of user
        LineItemEntity lineItemEntity = new LineItemEntity();
        LineItemIdKey linetItemIdKey = new LineItemIdKey();
        linetItemIdKey.setOrderId(ordersEntity.getOrderId());
        for (CartEntity cartEntity : cartEntityList) {
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
        //delete cart when add line-item success
        cartRepository.deleteByCartIdKey_Username(username);
    }

    public String createOrderId() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String orderId = timestamp.toString().substring(0, 10);
        orderId = orderId.replace("-", "");
        orderId = orderId + RandomString.make(6);
        return orderId;
    }
}

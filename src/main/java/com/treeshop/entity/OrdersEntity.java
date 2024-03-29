package com.treeshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.treeshop.entity.lineitem.CustomListLineItemSerializer;
import com.treeshop.entity.lineitem.LineItemEntity;
import com.treeshop.entity.review.ReviewsEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class OrdersEntity {
    @Id
    @Column(name = "order_id", length = 20)
    private String orderId;
    @Column(name = "contact_name", length = 100)
    private String contactName;
    @Column(name = "address", length = 200)
    private String address;
    @Column(name = "status", length = 100)
    @Enumerated(EnumType.STRING)
    private StatusOfOrder status;
    @Column(name = "phone_number", length = 10)
    private String phoneNumber;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "order_date")
    private String orderDate;
    @Column(name = "note")
    private String note;
    @Column(name = "user_name")
    private String username;
    @Column(name = "code_id")
    private String codeId;
    @Column(name = "total_price")
    private Integer totalPrice;
    @Column(name = "payment")
    private String payment;
    @Column(name = "shipper_id")
    private Integer shipperId;
    @Column(name = "review_flg")
    private boolean review_flg;
    @Column(name = "shipping_fee")
    private Integer shippingFee;
    @Column(name = "delivery_date")
    private String deliveryDate;

    @Transient
    private Integer subTotalPrice;

    @Transient
    private Integer discountPercent;

    @Transient
    public Integer getTotalOrder(){
        return totalPrice + shippingFee;
    }

    /**
     * @JsonIgnoreProperties (value = {"applications", "hibernateLazyInitializer"})
     * Đối với các entity mà có những thuộc tính ta định nghĩa là với Lazy loading,
     * khi cần dùng các API Json để xử lí thì @Annotation này giúp bỏ qua chuỗi hoặc thuộc tính rác,
     * không hữu ích mà Hibernate thêm vào, nên ta cần khai báo @Annotation này trước thuộc tính có Lazy loading.
     */
//    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_name", insertable = false, updatable = false)
    private UserEntity userEntity;

    //    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", insertable = false, updatable = false)
    private DiscountCodeEntity discountCodeEntity;

    @JsonSerialize(using = CustomListLineItemSerializer.class)
//    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ordersEntity")
    private List<LineItemEntity> lineItemEntityList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ordersEntity")
    private List<ReviewsEntity> reviewsEntityList;

    @JsonIgnore
    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "shipper_id", insertable = false, updatable = false)
    private ShipperEntity shipperEntity;
}

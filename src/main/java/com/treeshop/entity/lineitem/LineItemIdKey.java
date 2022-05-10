package com.treeshop.entity.lineitem;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LineItemIdKey implements Serializable{
    @Column(name = "order_id", length = 20)
    private String orderId;
    @Column(name = "product_id", length = 10)
    private String productId;


}

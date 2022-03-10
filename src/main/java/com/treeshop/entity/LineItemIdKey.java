package com.treeshop.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LineItemIdKey implements Serializable {
    @Column(length = 10)
    private String orderId;
    @Column(length = 10)
    private String productId;
}

package com.treeshop.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "discountcode")
public class DiscountCodeEntity {
    @Id
    @Column(name = "code_id", length = 10)
    private String codeId;
    @Column(name = "code_name", length = 200)
    private String codeName;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "discount_percent")
    private Integer discountPercent;
    @Column(name = "status", length = 100)
    private String status;


}

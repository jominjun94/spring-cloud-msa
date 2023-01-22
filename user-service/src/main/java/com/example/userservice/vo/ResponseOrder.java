package com.example.userservice.vo;

import lombok.Data;
import org.apache.catalina.util.Introspection;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.Date;

@Data
public class ResponseOrder implements Serializable {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId;


}

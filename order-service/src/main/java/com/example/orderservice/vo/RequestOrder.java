package com.example.orderservice.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class RequestOrder{
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;
}
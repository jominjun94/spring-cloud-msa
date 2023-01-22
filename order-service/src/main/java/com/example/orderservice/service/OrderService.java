package com.example.orderservice.service;


import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;

import java.util.List;

public interface OrderService {
    ResponseOrder createOrder(RequestOrder orderDetails);
    ResponseOrder getOrderByOrderId(String orderId);
    List<ResponseOrder> getOrdersByUserId(String userId);
}
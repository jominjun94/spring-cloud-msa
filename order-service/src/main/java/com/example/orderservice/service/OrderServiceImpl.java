package com.example.orderservice.service;

import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public ResponseOrder createOrder(RequestOrder requestOrder) {

        requestOrder.setOrderId(UUID.randomUUID().toString());
        requestOrder.setTotalPrice(requestOrder.getQty() * requestOrder.getUnitPrice());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = mapper.map(requestOrder, OrderEntity.class);

        orderRepository.save(orderEntity);

        ResponseOrder returnValue = mapper.map(orderEntity, ResponseOrder.class);

        return returnValue;
    }

    @Override
    public ResponseOrder getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        ResponseOrder responseOrder = new ModelMapper().map(orderEntity, ResponseOrder.class);

        return responseOrder;
    }

    @Override
    public List<ResponseOrder> getOrdersByUserId(String userId) {
        log.info("Before retrieve orders data");
        Iterable<OrderEntity> responseOrders = orderRepository.findByUserId(userId);
        List<ResponseOrder> result = new ArrayList<>();
        responseOrders.forEach(v->{
            result.add(new ModelMapper().map(v,ResponseOrder.class));
        });

        return result;
    }
}

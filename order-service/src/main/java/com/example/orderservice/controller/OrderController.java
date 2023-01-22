package com.example.orderservice.controller;
import com.example.orderservice.Kafka.KafkaProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-service")
@Slf4j
public class OrderController {

    Environment env;
    OrderService orderService;

    KafkaProducer kafkaProducer;
    @Autowired
    public OrderController(Environment env, OrderService orderService ,   KafkaProducer kafkaProducer
                           ) {
        this.env = env;
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }


    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,@RequestBody RequestOrder requestOrder){


        requestOrder.setUserId(userId);

        /*kafka*/
        log.info("Before kafka orders");
        kafkaProducer.send("example-catalog-topic", requestOrder);
        log.info("After kafka orders");

        return ResponseEntity.status(HttpStatus.OK).body(orderService.createOrder(requestOrder));
    }



    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) throws Exception {
        log.info("Add retrieved orders data");
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrdersByUserId(userId));
    }
}

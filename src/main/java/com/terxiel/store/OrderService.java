package com.terxiel.store;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService){
        this.paymentService = paymentService;
        System.out.println("Order Service Created");
    }

    @PostConstruct
    public void init()
    {
        System.out.println("PostConstruct method");
    }

    @PreDestroy
    public  void cleanup()
    {
        System.out.println("OrderService cleanup.");
    }

    public void placeOrder()
    {
        this.paymentService.processPayment(200);
    }
}

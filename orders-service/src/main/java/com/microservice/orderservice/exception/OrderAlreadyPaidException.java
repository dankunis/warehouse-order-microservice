package com.microservice.orderservice.exception;

public class OrderAlreadyPaidException extends RuntimeException {
    public OrderAlreadyPaidException(Long orderId) {
        super("Order already paid " + orderId + ". Cannot complete operation");
    }
}

package com.microservice.warehouse.exception;

import com.microservice.warehouse.domain.entity.BookedProduct;

public class PoductBookingNotExistingException extends RuntimeException{
    public PoductBookingNotExistingException(Long orderId) {
        super("Product booking with ID: " + orderId + "doesn't exist");
    }
}

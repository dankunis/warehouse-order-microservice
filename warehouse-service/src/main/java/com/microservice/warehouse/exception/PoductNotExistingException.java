package com.microservice.warehouse.exception;

import com.microservice.warehouse.domain.entity.BookedProduct;

public class PoductNotExistingException extends RuntimeException {
    public PoductNotExistingException(BookedProduct bookedProduct) {
        super(String.format("Product with ID: {} doesn't exist", bookedProduct.getProductId()));
    }
}

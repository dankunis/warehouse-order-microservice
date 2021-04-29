package com.microservice.warehouse.exception;

import com.microservice.warehouse.domain.entity.BookedProduct;
import com.microservice.warehouse.domain.entity.Product;

public class NotEnoughProductStockException extends RuntimeException {
    public NotEnoughProductStockException(Product product, BookedProduct order) {
        super(String.format("Product stock quantity: {} not enough for order: {}",
                product.getQuantity(),
                order.getQuantity()));
    }
}

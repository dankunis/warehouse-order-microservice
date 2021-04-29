package com.microservice.warehouse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.warehouse.domain.entity.BookedProduct;
import com.microservice.warehouse.domain.entity.Product;
import com.microservice.warehouse.exception.NotEnoughProductStockException;
import com.microservice.warehouse.exception.PoductBookingNotExistingException;
import com.microservice.warehouse.exception.PoductNotExistingException;
import com.microservice.warehouse.repository.BookedProductRepository;
import com.microservice.warehouse.repository.ProductRepository;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class WarehouseService {

  private final ObjectMapper objectMapper;
  private final ProductRepository productRepository;
  private final BookedProductRepository bookedProductRepository;
  private static final String ORDER_ID = "orderId";

  public void blockOrder(String message) {
    Map<String, Object> stringObjectMap = extractMapFromMessage(message);
    @SuppressWarnings("unchecked")
    Map<String, Integer> idQuantityProductMap =
        (Map<String, Integer>) stringObjectMap.get("idQuantityProductMap");

    Map<Long, Integer> copy = idQuantityProductMap
            .entrySet().stream().collect(Collectors.toMap(e -> Long.parseLong(e.getKey()),
            Map.Entry::getValue));

    var orderId = Long.valueOf((int) stringObjectMap.get(ORDER_ID));
    placeOrder(copy, orderId);
  }

  public void payOrder(String message) {
    Map<String, Object> stringObjectMap = extractMapFromMessage(message);
    var orderId = Long.valueOf((int) stringObjectMap.get(ORDER_ID));
    var bookedProduct = findBookedProductById(orderId);
    var product = findProductById(bookedProduct);

    if (product.getQuantity() < bookedProduct.getQuantity()) {
      log.error(
          "Product stock quantity: {} not enough for order: {}",
          product.getQuantity(),
          bookedProduct.getQuantity());
      throw new NotEnoughProductStockException(product, bookedProduct);
    }

    product.setQuantity(product.getQuantity() - bookedProduct.getQuantity());

    productRepository.save(product);
  }

  public void cancelOrder(String message) {
    Map<String, Object> stringObjectMap = extractMapFromMessage(message);
    var orderId = Long.valueOf((int) stringObjectMap.get(ORDER_ID));
    var bookedProduct = findBookedProductById(orderId);
    bookedProductRepository.delete(bookedProduct);
  }

  private Map<String, Object> extractMapFromMessage(String message) {
    Map<String, Object> stringObjectMap;
    try {
      stringObjectMap = objectMapper.readValue(message, new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      log.error("Deserialization error {}", e.getMessage(), e);
      throw new RuntimeException(e);
    }
    return stringObjectMap;
  }

  private BookedProduct createBookedProduct(Product product, Long orderId, Integer quantity) {
    return BookedProduct.builder()
        .orderId(orderId)
        .productId(product.getId())
        .quantity(quantity)
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();
  }

  private void placeOrder(Map<Long, Integer> copy, Long orderId) {
    var productList = productRepository.findAllById(copy.keySet());
    if (productList.isEmpty() || productList.size() != copy.size()) {
      log.error(
          "No bookings match order. Products: {}", productList.toString());
    }
    productList.stream()
        .map(
            product -> {
              if (product.getQuantity() < copy.get(product.getId())) {
                log.error(
                    "Not enough product stock {}, in order {}",
                    product.getQuantity(),
                    copy.get(product.getId()));
              }
              return createBookedProduct(product, orderId, copy.get(product.getId()));
            })
        .forEach(bookedProductRepository::save);
  }

  private Product findProductById(BookedProduct bookedProduct) {
    return productRepository
        .findById(bookedProduct.getProductId())
        .orElseThrow(
            () -> new PoductNotExistingException(bookedProduct));
  }

  private BookedProduct findBookedProductById(Long orderId) {
    return bookedProductRepository
        .findByOrderId(orderId)
        .orElseThrow(() -> new PoductBookingNotExistingException(orderId));
  }
}

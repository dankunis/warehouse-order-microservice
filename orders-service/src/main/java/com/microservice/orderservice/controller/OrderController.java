package com.microservice.orderservice.controller;

import com.microservice.orderservice.domain.dto.OrderDto;
import com.microservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping(path = "/find-orders")
  public ResponseEntity<List<OrderDto>> findOrders() {
    return ResponseEntity.ok(orderService.findOrders());
  }

  @PostMapping(path = "/create")
  public ResponseEntity<OrderDto> createOrder(
      @RequestBody Map<Long, Integer> idQuantityProductMap) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(orderService.createOrder(idQuantityProductMap));
  }

  @PatchMapping(path = "/pay/{orderId}")
  public ResponseEntity<Void> payOrder(@PathVariable @NumberFormat Long orderId) {
    orderService.payOrder(orderId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(path = "/cancel/{orderId}")
  public ResponseEntity<Void> cancelOrder(@PathVariable @NumberFormat Long orderId) {
    orderService.cancelOrder(orderId);
    return ResponseEntity.accepted().build();
  }
}

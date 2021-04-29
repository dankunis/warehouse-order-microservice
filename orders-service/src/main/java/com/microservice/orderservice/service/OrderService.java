package com.microservice.orderservice.service;

import com.microservice.orderservice.exception.OrderAlreadyPaidException;
import com.microservice.orderservice.exception.OrderNotFoundException;
import com.microservice.orderservice.repository.OrderRepository;
import com.microservice.orderservice.domain.dto.OrderDto;
import com.microservice.orderservice.domain.event.OrderCreatedEvent;
import com.microservice.orderservice.domain.event.OrderCanceledEvent;
import com.microservice.orderservice.domain.event.OrderPaidEvent;
import com.microservice.orderservice.domain.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private static final String ORDER_CREATED = "ORDER_CREATED";
  private static final String ORDER_PAID = "ORDER_PAID";
  private static final String ORDER_DELETED = "ORDER_DELETED";
  private final OrderRepository orderRepository;
  private final DomainEventManager domainEventManager;

  public List<OrderDto> findOrders() {
    var orders = orderRepository.findAll();
    if (orders.isEmpty()) {
      return Collections.emptyList();
    }
    return orders.stream().map(this::fromOrderToOrderDto).collect(Collectors.toList());
  }

  public OrderDto createOrder(Map<Long, Integer> idQuantityProductMap) {
    var savedOder = orderRepository.save(Order.create(idQuantityProductMap));

    domainEventManager.handleMessage(
        OrderCreatedEvent.builder()
            .orderId(savedOder.getId())
            .idQuantityProductMap(idQuantityProductMap)
            .build(),
        ORDER_CREATED);

    log.info("Oder created with ID {}", savedOder.getId());
    return fromOrderToOrderDto(savedOder);
  }

  public void payOrder(Long orderId) {
    var order = findOrderById(orderId);

    if (order.isPaid() || !order.getStatus().equalsIgnoreCase("CREATED")) {
      log.error("Order payment failed. Order status {}", order.getStatus());
      throw new OrderAlreadyPaidException(orderId);
    }
    order = order.pay();
    orderRepository.save(order);

    // send event to Kafka
    domainEventManager.handleMessage(OrderPaidEvent.builder().orderId(orderId).build(), ORDER_PAID);
    log.info("Order paid ID {}", order);
  }

  public void cancelOrder(Long orderId) {
    var order = findOrderById(orderId);

    if (order.isPaid() || !order.getStatus().equalsIgnoreCase("CREATED")) {
      log.error("Order cancellation failed. Order status {}", order.getStatus());
      throw new OrderAlreadyPaidException(orderId);
    }
    orderRepository.delete(order);

    domainEventManager.handleMessage(
        OrderCanceledEvent.builder().orderId(orderId).build(), ORDER_DELETED);
    log.info("Order cancelled ID {}", order);
  }

  private Order findOrderById(Long orderId) {
    return orderRepository
        .findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(orderId));
  }

  private OrderDto fromOrderToOrderDto(Order savedOder) {
    return OrderDto.builder()
        .id(savedOder.getId())
        .paid(savedOder.isPaid())
        .status(savedOder.getStatus())
        .products(null)
        .build();
  }
}

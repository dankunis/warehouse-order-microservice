package com.microservice.warehouse.controller;

import com.microservice.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseListener {

  private final WarehouseService warehouseService;

  @KafkaListener(topics = "ORDER_CREATED", groupId = "ms_group_id")
  public void onOrderCreated(String message) {
    log.info("Order is created {}", message);
    warehouseService.blockOrder(message);
  }

  @KafkaListener(topics = "ORDER_PAID", groupId = "ms_group_id")
  public void onOrderPaid(String message) {
    log.info("Order is paid {}", message);
    warehouseService.payOrder(message);
  }

  @KafkaListener(topics = "ORDER_CANCELLED", groupId = "ms_group_id")
  public void onOrderDeleted(String message) {
    log.info("Order is being cancelled {}", message);
    warehouseService.cancelOrder(message);
  }
}

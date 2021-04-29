package com.microservice.orderservice.domain.event;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder(toBuilder = true)
public class OrderCreatedEvent {
  Long orderId;
  Map<Long, Integer> idQuantityProductMap;
}

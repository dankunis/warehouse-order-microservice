package com.microservice.orderservice.domain.event;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class OrderPaidEvent {
  Long orderId;
}

package com.microservice.orderservice.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class OrderDto {
  Long id;
  List<ProductDto> products;
  boolean paid;
  String status;
}

package com.microservice.orderservice.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder(toBuilder = true)
class ProductDto {
  Long id;
  String title;
  String description;
  BigDecimal price;
}

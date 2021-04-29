package com.microservice.warehouse.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder(toBuilder = true)
public class ProductDto {
  Long id;
  String title;
  String description;
  BigDecimal price;
}

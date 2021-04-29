package com.microservice.orderservice.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  List<OrderItem> orderItems = new ArrayList<>();

  boolean paid;
  // Cancelled, Created or Paid
  String status;

  @Getter(AccessLevel.NONE)
  BigDecimal totalPrice;

  Instant createdAt;
  Instant updatedAt;

  public static Order create(Map<Long, Integer> idQuantityProductMap) {
    var orderedItems =
        idQuantityProductMap.entrySet().stream()
            .map(
                e -> OrderItem.builder().productId(e.getKey()).quantity(e.getValue()).build())
            .collect(Collectors.toList());

    return Order.builder()
        .status("CREATED")
        .orderItems(orderedItems)
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();
  }

  public BigDecimal getTotalPrice() {
//    if (orderItems == null || orderItems.isEmpty()) {
    return BigDecimal.ZERO;
//    }
//    return orderItems.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public Order pay() {
    return this.toBuilder().paid(true).status("PAID").updatedAt(Instant.now()).build();
  }
}

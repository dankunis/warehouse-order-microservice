package com.microservice.warehouse.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String title;
  String description;
  Integer quantity;
  @Version Long version;

  @Column(name = "created_at")
  Instant createdAt;
  @Column(name = "updated_at")
  Instant updatedAt;
}

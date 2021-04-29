package com.microservice.orderservice.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "order_products")
@NoArgsConstructor
class Product {
  @Id
  @Column(name = "id_product")
  Long idProduct;
}

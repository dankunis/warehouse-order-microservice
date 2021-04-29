package com.microservice.warehouse.repository;

import com.microservice.warehouse.domain.entity.BookedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookedProductRepository extends JpaRepository<BookedProduct, Long> {
  Optional<BookedProduct> findByOrderId(Long orderId);
}

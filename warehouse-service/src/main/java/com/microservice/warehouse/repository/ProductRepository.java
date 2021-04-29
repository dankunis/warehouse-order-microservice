package com.microservice.warehouse.repository;

import com.microservice.warehouse.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}

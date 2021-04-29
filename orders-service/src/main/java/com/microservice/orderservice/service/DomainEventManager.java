package com.microservice.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.orderservice.domain.event.OrderCreatedEvent;
import com.microservice.orderservice.domain.event.OrderCanceledEvent;
import com.microservice.orderservice.domain.event.OrderPaidEvent;
import com.microservice.orderservice.exception.KafkaUnexpectedEventException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DomainEventManager {

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired ObjectMapper objectMapper;

  void handleMessage(Object event, Object topic) {
    String topicName;
    if (event instanceof OrderCreatedEvent) {
      topicName = "ORDER_CREATED";
    } else if (event instanceof OrderPaidEvent) {
      topicName = "ORDER_PAID";
    } else if (event instanceof OrderCanceledEvent) {
      topicName = "ORDER_CANCELED";
    } else {
      throw new KafkaUnexpectedEventException(topic.toString());
    }

    try {
      kafkaTemplate.send(topicName, objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      log.error("Couldn't serialize event to send to Kafka {}", e.getLocation(), e);
      throw new RuntimeException(e);
    }
  }
}

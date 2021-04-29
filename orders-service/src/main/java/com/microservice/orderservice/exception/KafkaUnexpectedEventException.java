package com.microservice.orderservice.exception;

public class KafkaUnexpectedEventException extends RuntimeException {
    public KafkaUnexpectedEventException(String topicName) {
        super("Unexpected Kafka event " + topicName);

    }
}

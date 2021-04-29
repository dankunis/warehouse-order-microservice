package com.microservice.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class GatewayService {

  public static void main(String[] args) {
    SpringApplication.run(GatewayService.class, args);
  }
}

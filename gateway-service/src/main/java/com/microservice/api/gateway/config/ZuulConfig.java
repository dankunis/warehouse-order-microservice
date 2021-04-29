package com.microservice.api.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulConfig {
  @Bean
  public SimpleFilter simpleFilter() {
    return new SimpleFilter();
  }
}

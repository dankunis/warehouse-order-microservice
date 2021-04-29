package com.microservice.api.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import com.netflix.zuul.context.RequestContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = GatewayService.class)
class GatewayServiceTests {

  @Autowired private TestRestTemplate rest;
  private static ConfigurableApplicationContext ordersController;

  @BeforeAll
  public static void startBookService() {
    // стартую контроллер заказов
    ordersController = SpringApplication.run(OrdersController.class, "--server.port=8000");
  }

  @AfterAll
  public static void closeBookService() {
    ordersController.close();
  }

  @BeforeEach
  public void setup() {
    RequestContext.testSetCurrentContext(new RequestContext());
  }

  @Test
  void findOrders() {
    String resp = rest.getForObject("/orders/findOrders", String.class);
    assertThat(resp).isEqualTo("find orders works");
  }

  @Configuration
  @EnableAutoConfiguration
  @RestController
  static class OrdersController {
    @RequestMapping("/findOrders")
    public String findOrders() {
      return "find orders works";
    }
  }
}

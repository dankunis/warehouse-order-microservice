# warehouse-order-microservice

# Installation
```sh
docker-compose up
```

Then run all services in your IDE

# Dependencies

Requires docker, java, Postman and PGAdmin to run the demo

# API
```sh
[GET] localhost:8030/orders/find-orders -- find all orders
[POST] localhost:8030/orders/create -- create an order // {id: quantity}
[PATCH] localhost:8030/orders/pay/{id} -- payment
[PATCH] localhost:8030/orders/cancel/{id} -- cancellation
```

# Demo

1. Create connection in PGAdmin to the db (server: localhost, port: 5432, user & pass: postgres)
2. Send requests through Postman
3. See logs in IDE
4. Kafka is on http://localhost:9000

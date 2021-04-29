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

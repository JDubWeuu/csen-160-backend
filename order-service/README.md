# Ordering Microservice

This is the ordering microservice for the e-commerce backend system. It handles order creation and management, and communicates with the authentication service via Apache Kafka.

## Features

- Create orders
- Retrieve orders by ID
- Retrieve orders by user ID
- List all orders
- Kafka integration for inter-service communication

## Prerequisites

- Java 17
- Maven
- Apache Kafka (running on localhost:9092)

## Running the Service

1. Make sure Apache Kafka is running on `localhost:9092`
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the service:
   ```bash
   mvn spring-boot:run
   ```

The service will start on port **8081**.

## API Endpoints

- `GET /` - Health check
- `POST /api/orders` - Create a new order
- `GET /api/orders/{orderId}` - Get order by ID
- `GET /api/orders/user/{userId}` - Get all orders for a user
- `GET /api/orders` - Get all orders

## Kafka Topics

### Consumes from:
- `user-registered` - Listens for user registration events from the authentication service

### Publishes to:
- `order-created` - Publishes order creation events

## Example Request

```json
POST /api/orders
{
  "userId": 1,
  "items": [
    {
      "productId": "prod-123",
      "productName": "Sample Product",
      "quantity": 2,
      "price": 29.99
    }
  ]
}
```


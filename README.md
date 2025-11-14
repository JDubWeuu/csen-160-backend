# E-commerce Microservices Platform

This is a consolidated microservices platform with authentication and ordering services communicating via Apache Kafka.

## Project Structure

```
ecommerce-platform/
├── auth-service/          # Authentication microservice (port 8080)
├── order-service/         # Ordering microservice (port 8081)
├── cart-service/         # Cart microservice (port 8082)
├── nginx/                 # Nginx API Gateway configuration
│   └── nginx.conf
├── docker-compose.yml     # Kafka, Zookeeper, and Nginx setup
└── README.md              # documentation
```

## Quick Start

### 1. Start Infrastructure (Kafka, Zookeeper, and Nginx API Gateway)

```bash
docker-compose up -d
```

### 2. Start Authentication Service

```bash
cd auth-service
mvn spring-boot:run
```

### 3. Start Ordering Service

```bash
cd order-service
mvn spring-boot:run
```

### 4. Start Cart Service

```bash
cd cart-service
mvn spring-boot:run
```

### 5. Access Services via API Gateway

All requests should go through the Nginx API Gateway at **http://localhost**

- Auth endpoints: `http://localhost/api/auth/*`
- Order endpoints: `http://localhost/api/orders/*`
- Cart endpoints: `http://localhost/api/cart/*`
- AI profile summary: `http://localhost/api/orders/profile/summary`

## Services

- **Nginx API Gateway** (port 80): Routes all API requests to appropriate microservices
- **Auth Service** (port 8080): User registration, login, JWT authentication
- **Order Service** (port 8081): Order creation and management
- **Cart Service** (port 8082): Adding items to cart and clearing items from cart

Both services communicate via Kafka topics:

- `user-registered`: Auth service → Order service
- `order-created`: Order service → (available for other services)

## API Gateway Routing

The Nginx API Gateway routes requests as follows:

- `/api/auth/*` → Auth Service (port 8080)
- `/api/orders/*` → Order Service (port 8081)
- `/api/cart/*` → Order Service (port 8082)

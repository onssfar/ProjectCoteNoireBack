# Côte Noire Backend
Spring Boot 3.5.3 + Java 21 + MySQL backend for the Côte Noire frontend.

## Setup
1. Create database: `CREATE DATABASE cote_noire CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
2. Edit `src/main/resources/application.properties` and set the MySQL password.
3. Run `mvn spring-boot:run`.

## API
GET `/api/health`
GET `/api/products`
GET `/api/products/{id}`
POST `/api/orders`
GET `/api/orders/{id}`
GET `/api/orders/number/{orderNumber}`
PATCH `/api/orders/{id}/status?status=SHIPPED`
PATCH `/api/payments/{id}/paid`

## Order
The client sends product IDs and quantities only. Prices, stock, delivery fee and total are calculated server-side.

Payment method: `CASH_ON_DELIVERY`.
Payment starts as `PENDING` and can be marked `PAID` after delivery.

Standard delivery: 6 TND under 90 TND, free from 90 TND.
Express delivery: 12 TND.

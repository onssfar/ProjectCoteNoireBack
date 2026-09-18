package com.cotenoire.dto;

import com.cotenoire.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        String orderNumber,
        OrderStatus status,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        DeliveryMethod deliveryMethod,
        BigDecimal subtotal,
        BigDecimal deliveryFee,
        BigDecimal total,
        String currency,
        LocalDateTime createdAt,
        CustomerResponse customer,
        List<OrderItemResponse> items
) {
}
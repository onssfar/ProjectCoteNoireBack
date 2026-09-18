package com.cotenoire.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String productName,
        String image,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal total
) {}
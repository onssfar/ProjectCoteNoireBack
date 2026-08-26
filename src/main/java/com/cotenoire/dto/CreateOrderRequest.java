package com.cotenoire.dto;

import com.cotenoire.enums.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateOrderRequest(@Valid @NotNull CustomerRequest customer,
                                 @Valid @NotEmpty List<OrderItemRequest> items, @NotNull DeliveryMethod deliveryMethod,
                                 @NotNull PaymentMethod paymentMethod, String giftMessage) {
}

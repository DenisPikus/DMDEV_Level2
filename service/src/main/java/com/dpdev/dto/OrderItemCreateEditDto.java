package com.dpdev.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Value
@Builder
public class OrderItemCreateEditDto {

    @NonNull
    Long ordersId;

    @NonNull
    Long productId;

    @NonNull
    @Positive
    Integer quantity;

    BigDecimal price;
}

package com.dpdev.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderItemReadDto {
    Long id;
    OrdersReadDto ordersReadDto;
    ProductReadDto productReadDto;
    Integer quantity;
    BigDecimal price;
}

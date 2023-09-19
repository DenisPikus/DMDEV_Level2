package com.dpdev.dto;

import com.dpdev.entity.enums.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class OrdersReadDto {
    Long id;
    UserReadDto user;
    Instant creationDate;
    Instant closingDate;
    OrderStatus orderStatus;
    List<OrderItemReadDto> orderProductDtos;
}

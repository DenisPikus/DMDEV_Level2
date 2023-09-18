package com.dpdev.dto;

import com.dpdev.entity.enums.OrderStatus;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class OrdersCreateEditDto {

    @NonNull
    Long userId;

    @NonNull
    Instant creationDate;

    Instant closingDate;

    OrderStatus orderStatus;

    List<Long> ordersIds;
}

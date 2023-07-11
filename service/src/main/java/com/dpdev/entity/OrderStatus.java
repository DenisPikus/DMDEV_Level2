package com.dpdev.entity;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    COMPLETED;

    public static OrderStatus findByName(String name) {
        return findByNameOpt(name).orElseThrow();
    }

    public static Optional<OrderStatus> findByNameOpt(String name) {
        return Arrays.stream(values())
                .filter(orderStatus -> orderStatus.name().equalsIgnoreCase(name))
                .findFirst();
    }
}

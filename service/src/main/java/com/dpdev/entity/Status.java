package com.dpdev.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Status {
    PENDING,
    PROCESSING,
    COMPLETED;

    public static Status findByName(String name) {
        return findByNameOpt(name).orElseThrow();
    }

    public static Optional<Status> findByNameOpt(String name) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(name))
                .findFirst();
    }
}

package com.dpdev.entity;

import java.util.Arrays;
import java.util.Optional;

public enum ProductType {
    ROD,
    REEL,
    BAIT;

    public static ProductType findByName(String name) {
        return findByNameOpt(name).orElseThrow();
    }

    public static Optional<ProductType> findByNameOpt(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst();
    }
}

package com.dpdev.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Type {
    ROD,
    REEL,
    BAIT;

    public static Type findByName(String name) {
        return findByNameOpt(name).orElseThrow();
    }

    public static Optional<Type> findByNameOpt(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst();
    }
}

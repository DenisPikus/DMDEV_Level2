package com.dpdev.entity.enums;

import java.util.Arrays;
import java.util.Optional;


public enum Brand {
    SHIMANO,
    ZIPBAITS,
    MEGABASS,
    GANCRAFT,
    LUCKYCRAFT,
    JACKALL;

    public static Brand findByName(String name) {
        return findByNameOpt(name)
                .orElseThrow();
    }

    public static Optional<Brand> findByNameOpt(String name) {
        return Arrays.stream(values())
                .filter(brand -> brand.name().equalsIgnoreCase(name))
                .findFirst();
    }
}

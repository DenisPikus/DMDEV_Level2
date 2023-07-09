package com.dpdev.entity;

import java.util.Arrays;
import java.util.Optional;


public enum Brand {
    SHIMANO,
    ZIPBAITS,
    MEGABASS,
    GANCRAFT,
    LUCKYCRAFT,
    JACKALL;

//    private String name;
//    Brand(String name) {
//        this.name = name;
//    }
//
//    public String getName() {
//        return name;
//    }

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

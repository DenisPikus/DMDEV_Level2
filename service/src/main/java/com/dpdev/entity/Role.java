package com.dpdev.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    USER, ADMIN;

    public static Role findByName(String name) {
        return findByNameOpt(name).orElseThrow();
    }

    public static Optional<Role> findByNameOpt(String name) {
        return Arrays.stream(values())
                .filter(role -> role.name().equalsIgnoreCase(name))
                .findFirst();
    }
}

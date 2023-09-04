package com.dpdev.entity.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Optional;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    public static Role findByName(String name) {
        return findByNameOpt(name).orElseThrow();
    }

    public static Optional<Role> findByNameOpt(String name) {
        return Arrays.stream(values())
                .filter(role -> role.name().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public String getAuthority() {
        return name();
    }
}

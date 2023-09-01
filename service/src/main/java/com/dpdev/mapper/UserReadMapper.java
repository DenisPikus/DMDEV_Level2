package com.dpdev.mapper;

import com.dpdev.dto.UserReadDto;
import com.dpdev.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User object) {
        return UserReadDto.builder()
                .id(object.getId())
                .firstname(object.getFirstname())
                .lastname(object.getLastname())
                .email(object.getEmail())
                .phoneNumber(object.getPhoneNumber())
                .address(object.getAddress())
                .role(object.getRole())
                .image(object.getImage())
                .build();
    }
}

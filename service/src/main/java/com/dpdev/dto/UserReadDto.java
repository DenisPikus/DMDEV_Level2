package com.dpdev.dto;

import com.dpdev.entity.enums.Role;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserReadDto {
    Long id;
    String firstname;
    String lastname;
    String username;
    String phoneNumber;
    String address;
    Role role;
    String image;
}

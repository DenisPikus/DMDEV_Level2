package com.dpdev.dto;

import com.dpdev.entity.enums.Role;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
@Builder
public class UserCreateEditDto {
    String firstname;
    String lastname;
    String email;
    String password;
    String phoneNumber;
    String address;
    Role role;
}

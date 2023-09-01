package com.dpdev.dto;

import com.dpdev.entity.enums.Role;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Value
@FieldNameConstants
@Builder
public class UserCreateEditDto {

    @Size(min = 2, max = 64)
    String firstname;

    @Size(min = 2, max = 64)
    String lastname;

    @Email
    String email;

    String password;

    @Size(max = 13)
    String phoneNumber;

    @Size(min = 10, max = 128)
    String address;

    @NonNull
    Role role;

    MultipartFile image;
}

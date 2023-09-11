package com.dpdev.dto;

import com.dpdev.entity.enums.Role;
import com.dpdev.validation.group.CreateAction;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
    String username;

    @NotBlank(groups = CreateAction.class)
    String rawPassword;

    @Size(max = 13)
    String phoneNumber;

    @Size(min = 10, max = 128)
    String address;

    Role role;

    MultipartFile image;
}

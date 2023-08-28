package com.dpdev.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginDto {
    String email;
    String password;
}

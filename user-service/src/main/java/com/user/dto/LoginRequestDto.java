package com.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
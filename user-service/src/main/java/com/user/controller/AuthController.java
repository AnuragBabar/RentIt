package com.user.controller;

import org.springframework.web.bind.annotation.*;

import com.user.dto.LoginRequestDto;
import com.user.dto.LoginResponseDto;
import com.user.service.userServiceInterface;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final userServiceInterface service;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto dto) {
        return service.login(dto);
    }
}

package com.user.controller;

import org.springframework.web.bind.annotation.*;
import com.user.dto.UserRequestDto;
import com.user.dto.UserResponseDto;
import com.user.service.userServiceInterface;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final userServiceInterface service;

    @PostMapping("/register")
    public UserResponseDto register(@Valid @RequestBody UserRequestDto dto) {
        return service.registerUser(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("/display")
    public java.util.List<UserRequestDto> display() {
        return service.display();
    }
}

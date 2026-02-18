package com.user.service;

import java.util.List;

import com.user.dto.LoginRequestDto;
import com.user.dto.LoginResponseDto;
import com.user.dto.UserRequestDto;
import com.user.dto.UserResponseDto;

public interface userServiceInterface {

	public UserResponseDto registerUser(UserRequestDto userDto);
	
	public UserResponseDto getUserById(Long id);
	

	public List<UserRequestDto> display();
	
	LoginResponseDto login(LoginRequestDto dto);
	
}

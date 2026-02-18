package com.user.dto;

import com.user.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

	private long userId;
	private String userName;
	private String userEmail;
	private String phNo;
	private Role role;
}

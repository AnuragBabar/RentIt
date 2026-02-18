package com.user.dto;

import com.user.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

	@NotBlank(message = "User name is required")
	private String userName;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String userEmail;

	@NotBlank(message = "Phone number is required")
	private String phNo;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	@NotNull(message = "Role is required")
	private Role role;
}

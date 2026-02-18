package com.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.user.dto.LoginRequestDto;
import com.user.dto.LoginResponseDto;
import com.user.dto.UserRequestDto;
import com.user.dto.UserResponseDto;
import com.user.entity.User;
import com.user.exception.UserNotFoundException;
import com.user.repository.UserRepository;
import com.user.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class userServiceImplementation implements userServiceInterface {

	private final ModelMapper modelMapper;
	private final UserRepository userRepo;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserResponseDto registerUser(UserRequestDto userDto) {
		System.out.println("Processing registration for: " + userDto.getUserEmail());
		if (userRepo.findByUserEmail(userDto.getUserEmail()).isPresent()) {
			System.out.println("Registration failed: Email already exists");
			throw new RuntimeException("Email already registered");
		}
		User user = modelMapper.map(userDto, User.class);
		// Hashing the password using BCrypt
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		User savedUser = userRepo.save(user);
		System.out.println("Registration successful: " + savedUser.getUserId());
		return modelMapper.map(savedUser, UserResponseDto.class);
	}

	@Override
	public UserResponseDto getUserById(Long id) {
		User user = userRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
		return modelMapper.map(user, UserResponseDto.class);
	}

	@Override
	public LoginResponseDto login(LoginRequestDto dto) {

		User user = userRepo.findByUserEmail(dto.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid email or password"));

		if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid email or password");
		}

		String token = jwtUtil.generateToken(
				user.getUserId(),
				user.getUserEmail(),
				user.getRole().name());

		return new LoginResponseDto(token, user.getRole().name(), user.getUserId());
	}

	@Override
	public List<UserRequestDto> display() {
		List<User> userList = userRepo.findAll();

		return userList.stream()
				.map(user -> modelMapper.map(user, UserRequestDto.class))
				.collect(Collectors.toList());
	}

}

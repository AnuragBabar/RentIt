package com.item.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
	
	private LocalDateTime timestamp;
	private int status;
	private String message;

}

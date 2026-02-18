package com.booking.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
	
	private LocalDateTime timeStamp;
	private int status;
	private String message;

}

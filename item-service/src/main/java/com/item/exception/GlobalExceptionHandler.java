package com.item.exception;

import java.time.LocalDateTime;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ApiError> handleItemNotFound(ItemNotFoundException exception){
		
		ApiError error = new ApiError(
				LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				exception.getMessage()
				);
		
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException exception){
		String message = exception.getBindingResult()
				.getFieldErrors()
				.get(0)
				.getDefaultMessage();
		
		ApiError error = new ApiError(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				message);
		
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneric(Exception exception){
		ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage()
        );
		
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoryNotFound(CategoryNotFoundException exception) {
        return new ResponseEntity<>(
            new ApiError(LocalDateTime.now(), 404, exception.getMessage()),
            HttpStatus.NOT_FOUND
        );
    }
	
	@ExceptionHandler(ItemAlreadyBookedException.class)
	public ResponseEntity<ApiError> handleItemAlreadyBooked(ItemAlreadyBookedException ex) {
	    return new ResponseEntity<>(
	        new ApiError(LocalDateTime.now(), 400, ex.getMessage()),
	        HttpStatus.BAD_REQUEST
	    );
	}
}

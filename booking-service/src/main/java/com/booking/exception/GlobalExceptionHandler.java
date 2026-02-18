package com.booking.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(BookingNotFoundException.class)
	public ResponseEntity<ApiError> handleBookingNotFound(BookingNotFoundException exception){
		return new ResponseEntity<>(
				new ApiError(LocalDateTime.now(), 404, exception.getMessage()),
				HttpStatus.NOT_FOUND);
	}
	 
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
	    String msg = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(
        		new ApiError(LocalDateTime.now(), 400, msg),
        		HttpStatus.BAD_REQUEST);
	 }
	
	@ExceptionHandler(ItemNotAvailableException.class)
	public ResponseEntity<ApiError> handleItemNotAvailable(ItemNotAvailableException ex) {

	    ApiError error = new ApiError(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage()
	    );

	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidBookingStateException.class)
	public ResponseEntity<ApiError> handleBookingStateInvalid(InvalidBookingStateException ex) {

	    ApiError error = new ApiError(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage()
	    );

	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}

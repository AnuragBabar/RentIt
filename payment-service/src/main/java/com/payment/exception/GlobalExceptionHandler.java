package com.payment.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	 @ExceptionHandler(InvalidPaymentException.class)
	    public ResponseEntity<ApiError> handleInvalidPayment(InvalidPaymentException ex) {

	        ApiError error = new ApiError(
	                LocalDateTime.now(),
	                HttpStatus.BAD_REQUEST.value(),
	                ex.getMessage()
	        );

	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(DuplicatePaymentException.class)
	    public ResponseEntity<ApiError> handleDuplicatePayment(DuplicatePaymentException ex) {

	        ApiError error = new ApiError(
	                LocalDateTime.now(),
	                HttpStatus.CONFLICT.value(),
	                ex.getMessage()
	        );

	        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	    }

	    @ExceptionHandler(BookingNotFoundException.class)
	    public ResponseEntity<ApiError> handleBookingNotFound(BookingNotFoundException ex) {

	        ApiError error = new ApiError(
	                LocalDateTime.now(),
	                HttpStatus.NOT_FOUND.value(),
	                ex.getMessage()
	        );

	        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	    }

//	    @ExceptionHandler(Exception.class)
//	    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
//
//	        ApiError error = new ApiError(
//	                LocalDateTime.now(),
//	                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//	                "Internal server error"
//	        );
//
//	        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
	    
	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ApiError> handleGeneric(Exception ex) {

	        // 🔴 TEMPORARY DEBUG
	        ex.printStackTrace();

	        return new ResponseEntity<>(
	                new ApiError(
	                        LocalDateTime.now(),
	                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                        ex.getMessage()
	                ),
	                HttpStatus.INTERNAL_SERVER_ERROR
	        );
	    }


}

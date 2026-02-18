package com.payment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.dto.PaymentRequestDto;
import com.payment.dto.PaymentResponseDto;
import com.payment.security.RoleValidator;
import com.payment.service.PaymentServiceInterface;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {
	
	PaymentServiceInterface service;

	@PostMapping
	public PaymentResponseDto makePayment(@Valid @RequestBody PaymentRequestDto paymentDto , HttpServletRequest request) {
		RoleValidator.requireRole(request, "RENTER");
		return service.makePayment(paymentDto);
	}	
}

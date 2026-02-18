package com.payment.dto;

import com.payment.entity.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

	private long id;
	private Long bookingId;
	private Double amount;
	private PaymentStatus status;
}

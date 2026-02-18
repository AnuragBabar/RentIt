package com.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class PaymentRequestDto {

	@NotNull
	private Long bookingId;
	
	@NotNull
	private Double amount;
}

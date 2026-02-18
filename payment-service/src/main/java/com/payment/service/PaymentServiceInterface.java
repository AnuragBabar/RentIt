package com.payment.service;

import com.payment.dto.*;

public interface PaymentServiceInterface {

	public PaymentResponseDto makePayment(PaymentRequestDto paymentDto);
	
}

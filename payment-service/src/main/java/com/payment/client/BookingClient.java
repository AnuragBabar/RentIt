package com.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.payment.client.dto.BookingResponseDto;

@FeignClient(name = "booking-service", url = "http://localhost:8083")
public interface BookingClient {

	@GetMapping("/bookings/{id}")
	BookingResponseDto getBookingById(@PathVariable Long id);

	@PutMapping("/bookings/{id}/confirm")
	void confirmBooking(@PathVariable Long id);

}
